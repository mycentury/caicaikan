/**
 * 
 */
package win.caicaikan.service.external;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import win.caicaikan.util.TypeConverterUtil;

/**
 * @Desc
 * @author wewenge.yan
 * @Date 2016年11月22日
 * @ClassName HttpService
 */
@Service
public class HttpService {

	public String assembleHttpGetUrlWithOrder(Object obj) throws UnsupportedEncodingException {
		StringBuilder sb = new StringBuilder("");
		if (StringUtils.isEmpty(obj)) {
			return sb.toString();
		}
		Map<String, Object> map = TypeConverterUtil.changeSourceToMap(obj);
		if (CollectionUtils.isEmpty(map)) {
			return sb.toString();
		}
		Object[] keys = new ArrayList<String>(map.keySet()).toArray();
		for (int i = 0; i < keys.length - 1; i++) {
			for (int j = keys.length - 1; j > i; j--) {
				Object temp = keys[j];
				if (temp.toString().compareTo(keys[j - 1].toString()) < 0) {
					keys[j] = keys[j - 1];
					keys[j - 1] = temp;
				}
			}
		}
		for (Object object : keys) {
			if (!StringUtils.isEmpty(map.get(object))) {
				sb.append("&").append(percentEncode(object.toString())).append("=")
						.append(percentEncode(map.get(object).toString()));
			}
		}
		return sb.toString().substring(1);
	}

	public String assembleHttpGetUrl(Object obj) {
		StringBuilder sb = new StringBuilder("");
		if (StringUtils.isEmpty(obj)) {
			return sb.toString();
		}
		Class<? extends Object> clazz = obj.getClass();
		boolean isFirst = true;
		while (!clazz.equals(Object.class)) {
			Field fields[] = clazz.getDeclaredFields();
			for (Field field : fields) {
				try {
					field.setAccessible(true);
					if (field.get(obj) != null) {
						if (isFirst) {
							isFirst = false;
						} else {
							sb.append("&");
						}
						sb.append(field.getName()).append("=").append(field.get(obj));// 读取属性值
					}
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
			}
			clazz = clazz.getSuperclass();
		}
		return sb.toString();
	}

	/**
	 * 用Jsoup方法
	 * 
	 * @param url
	 * @return
	 */
	public Document getDocumentByGetWithJsoup(String url) {
		try {
			return Jsoup.connect(url).get();
		} catch (IOException e) {
		}
		return null;
	}

	/**
	 * 用HttpClient方法
	 * 
	 * @param url
	 * @return
	 */
	public String getDocumentByGetWithHttpClient(String url) {
		HttpClient httpClient = new DefaultHttpClient();
		try {
			HttpResponse response = httpClient.execute(new HttpGet(url));
			return EntityUtils.toString(response.getEntity());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public String percentEncode(String value) throws UnsupportedEncodingException {
		return value != null ? URLEncoder.encode(value, Charset.defaultCharset().name())
				.replace("+", "%20").replace("*", "%2A").replace("%7E", "~") : null;
	}// 中可对 ECS 资源进行授权的 Action 中才能使用此参数，否则访问会被拒绝）
}

/**
 * 
 */
package win.caicaikan.service.external;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;

/**
 * @Desc
 * @author wewenge.yan
 * @Date 2016年11月22日
 * @ClassName HttpService
 */
@Service
public class HttpService {
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
}

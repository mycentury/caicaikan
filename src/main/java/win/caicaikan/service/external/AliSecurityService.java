/**
 * 
 */
package win.caicaikan.service.external;

import java.io.UnsupportedEncodingException;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import win.caicaikan.service.external.domain.AliSecurityQueryReq;
import win.caicaikan.service.external.domain.AliSecurityQueryRes;

import com.google.gson.Gson;

/**
 * @Desc
 * @author wewenge.yan
 * @Date 2016年12月19日
 * @ClassName AliSecurityService
 */
@Service
public class AliSecurityService {
	@Autowired
	private HttpService httpService;
	private String apiUrl = "https://ecs.aliyuncs.com/";

	public AliSecurityQueryRes querySecurityRules(AliSecurityQueryReq req) {
		String url;
		try {
			url = httpService.assembleHttpGetUrlWithOrder(req);
			String stringToSign = this.generateStringToSign("GET", url);
			String signature = this
					.getSignature("HmacSHA1", stringToSign, req.getAccessKeySecret());
			req.setSignature(signature);
			url = httpService.assembleHttpGetUrlWithOrder(req);
			String json = httpService.getDocumentByGetWithHttpClient(apiUrl + "?" + url);
			AliSecurityQueryRes res = new Gson().fromJson(json, AliSecurityQueryRes.class);
			return res;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;
		}
	}

	public String generateStringToSign(String method, String queryString) {
		try {
			return method + "&" + httpService.percentEncode("/") + "&"
					+ httpService.percentEncode(queryString);
		} catch (Exception e) {
			return null;
		}
	}

	private String getSignature(String SignatureMethod, String stringToSign, String key) {
		try {
			Mac mac = Mac.getInstance(SignatureMethod);
			mac.init(new SecretKeySpec((key + "&").getBytes("UTF-8"), SignatureMethod));
			byte[] signData = mac.doFinal(stringToSign.getBytes("UTF-8"));
			String signature = new String(new sun.misc.BASE64Encoder().encode(signData));
			return signature;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}

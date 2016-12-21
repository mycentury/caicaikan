/**
 * 
 */
package win.caicaikan.service.external;

import java.io.UnsupportedEncodingException;

import org.springframework.stereotype.Service;

import win.caicaikan.service.external.domain.AliSecurityQueryReq;
import win.caicaikan.service.external.domain.AliSecurityQueryRes;
import win.caicaikan.service.external.domain.AliSecurityReq;
import win.caicaikan.service.external.domain.AliSecurityRes;

import com.google.gson.Gson;

/**
 * @Desc
 * @author wewenge.yan
 * @Date 2016年12月19日
 * @ClassName AliSecurityService
 */
@Service
public class AliApiSecurityService extends AliApiBaseService {
	private String apiUrl = "https://ecs.aliyuncs.com/";

	public AliSecurityQueryRes querySecurityRules(AliSecurityQueryReq req) {
		try {
			req.setSignature(null);
			String url = httpService.assembleHttpGetUrlWithOrder(req);
			String stringToSign = this.generateStringToSign("GET", url);
			String signature = this.getSignature("HmacSHA1", stringToSign, req.getAccessKeySecret());
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

	public AliSecurityRes updateSecurityRule(AliSecurityReq req) {
		try {
			String url = httpService.assembleHttpGetUrlWithOrder(req);
			String stringToSign = this.generateStringToSign("GET", url);
			String signature = this.getSignature("HmacSHA1", stringToSign, req.getAccessKeySecret());
			req.setSignature(signature);
			url = httpService.assembleHttpGetUrlWithOrder(req);
			String json = httpService.getDocumentByGetWithHttpClient(apiUrl + "?" + url);
			AliSecurityRes res = new Gson().fromJson(json, AliSecurityRes.class);
			return res;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;
		}
	}
}

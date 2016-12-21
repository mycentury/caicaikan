/**
 * 
 */
package win.caicaikan.service.external;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Desc
 * @author wewenge.yan
 * @Date 2016年12月19日
 * @ClassName AliSecurityService
 */
@Service
public class AliApiBaseService {
	@Autowired
	protected HttpService httpService;

	protected String generateStringToSign(String method, String queryString) {
		try {
			return method + "&" + httpService.percentEncode("/") + "&" + httpService.percentEncode(queryString);
		} catch (Exception e) {
			return null;
		}
	}

	protected String getSignature(String SignatureMethod, String stringToSign, String key) {
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

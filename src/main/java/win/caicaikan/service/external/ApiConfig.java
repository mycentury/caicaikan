/**
 * 
 */
package win.caicaikan.service.external;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * @Desc
 * @author wewenge.yan
 * @Date 2016年12月20日
 * @ClassName ApiConfigService
 */
@Service
public class ApiConfig {
	@Value("${aliyun.api.format}")
	private String Format; // 否 返回值的类型，支持 JSON 与 XML。默认为 XML。
	@Value("${aliyun.api.version}")
	private String Version; // 是 API 版本号，为日期形式：YYYY-MM-DD，本版本对应为 2014-05-26。
	@Value("${aliyun.api.access.key}")
	private String AccessKeyID; // 是 阿里云颁发给用户的访问服务所用的密钥 ID。
	@Value("${aliyun.api.access.secret}")
	private String AccessKeySecret; // 是 阿里云颁发给用户的访问服务所用的密钥 ID。
	@Value("${aliyun.api.signature.method}")
	private String SignatureMethod; // 是 签名方式，目前支持 HMAC-SHA1。
	// YYYY-MM-DDThh:mm:ssZ
	// 例如，2014-05-26T12:00:00Z（为北京时间 2014 年 5 月 26 日 20 点 0 分 0 秒）。
	@Value("${aliyun.api.signature.version}")
	private String SignatureVersion; // 是 签名算法版本，目前版本是 1.0。
	@Value("${aliyun.security.group.id}")
	private String SecurityGroupId;// 必传。目标安全组编码
	@Value("${aliyun.security.region.id}")
	private String RegionId;// 必传。目标安全组所属Region ID

	public String getFormat() {
		return Format;
	}

	public String getVersion() {
		return Version;
	}

	public String getAccessKeyID() {
		return AccessKeyID;
	}

	public String getAccessKeySecret() {
		return AccessKeySecret;
	}

	public String getSignatureMethod() {
		return SignatureMethod;
	}

	public String getSignatureVersion() {
		return SignatureVersion;
	}

	public String getSecurityGroupId() {
		return SecurityGroupId;
	}

	public String getRegionId() {
		return RegionId;
	}
}

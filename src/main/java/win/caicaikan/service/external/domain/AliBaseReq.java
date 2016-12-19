/**
 * 
 */
package win.caicaikan.service.external.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @Desc
 * @author wewenge.yan
 * @Date 2016年12月19日
 * @ClassName AliSecurityReq
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class AliBaseReq {
	private String Format; // 否 返回值的类型，支持 JSON 与 XML。默认为 XML。
	private String Version; // 是 API 版本号，为日期形式：YYYY-MM-DD，本版本对应为 2014-05-26。
	private String AccessKeyID; // 是 阿里云颁发给用户的访问服务所用的密钥 ID。
	private String AccessKeySecret; // 是 阿里云颁发给用户的访问服务所用的密钥 ID。
	private String Signature; // 是 签名结果串，关于签名的计算方法，请参见<签名机制>。
	private String SignatureMethod; // 是 签名方式，目前支持 HMAC-SHA1。
	private String Timestamp; // 是 请求的时间戳。日期格式按照 ISO8601 标准表示，并需要使用 UTC 时间。格式为：
								// YYYY-MM-DDThh:mm:ssZ
	// 例如，2014-05-26T12:00:00Z（为北京时间 2014 年 5 月 26 日 20 点 0 分 0 秒）。
	private String SignatureVersion; // 是 签名算法版本，目前版本是 1.0。
	private String SignatureNonce; // 是 唯一随机数，用于防止网络重放攻击。用户在不同请求间要使用不同的随机数值
	private String ResourceOwnerAccount; // 否 本次 API 请求访问到的资源拥有者账户，即登录用户名。
											// 此参数的使用方法，详见< 借助 RAM 实现子账号对主账号的
											// ECS 资源访问
}

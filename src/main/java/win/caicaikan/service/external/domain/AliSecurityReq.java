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
public class AliSecurityReq extends AliBaseReq {
	private String Action = "AuthorizeSecurityGroup";// 必传。系统规定参数，取值：AuthorizeSecurityGroup|AuthorizeSecurityGroupEgress|RevokeSecurityGroup|RevokeSecurityGroupEgress
	private String SecurityGroupId;// 必传。目标安全组编码
	private String RegionId;// 必传。目标安全组所属Region ID
	private String IpProtocol = "all";// SourceCidrIpIP协议
	private String PortRange;// SourceCidrIp端口范围
	private String SourceGroupId;// 源安全组，用于Ingress授权
	private String SourceGroupOwnerAccount;// 源安全组所属阿里云账户Id
	private String SourceCidrIp;// 源IP地址段，用于Ingress授权
	private String Policy = "accept";// 授权策略
	private String Priority = "1";// 规则优先级
	private String NicType = "internet";// 网络类型
}

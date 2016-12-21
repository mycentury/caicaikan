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
public class AliSecurityQueryReq extends AliBaseReq {
	private String Action = "DescribeSecurityGroupAttribute";// 必传。系统规定参数，取值：AuthorizeSecurityGroup
	private String SecurityGroupId;// 必传。目标安全组编码
	private String RegionId;// 必传。目标安全组所属Region ID
	private String NicType;// 可传。"网络类型，取值：internet,intranet默认值为internet,当对安全组进行相互授权时（即指定了SourceGroupId且没有指定SourceCidrIp），必须指定NicType为intranet
	private String Direction;// 否 授权方向，取值：egress|ingress|all，默认值为all
}

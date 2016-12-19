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
	private String Action = "AuthorizeSecurityGroup";// 必传。系统规定参数，取值：AuthorizeSecurityGroup
	private String SecurityGroupId;// 必传。目标安全组编码
	private String RegionId;// 必传。目标安全组所属Region ID
	private String IpProtocol = "all";// 必传。IP协议，取值：tcp | udp | icmp | gre | all；all表示同时支持四种协议
	private String PortRange = "-1/-1";// 必传。"IP协议相关的端口号范围 协议为tcp、udp时默认端口号，取值范围为1~65535； 例如“1/200”意思是端口号范围为1~200，若输入值为：“200/1”接口调用将报错。协议为icmp时端口号范围值为-1/-1； gre协议时端口号范围值为-1/-1； 协议为all时端口号范围值为-1/-1"
	private String SourceGroupId;// 可传。源安全组ID，SourceGroupId或者SourceCidrIp参数必须设置一项，如果两项都设置，则默认对SourceCidrIp授权。如果指定了该字段且没有指定SourceCidrIp，则NicType只能选择intranet
	private String SourceGroupOwnerAccount;// 可传。跨用户安全组授权时，源安全组所属用户的阿里云账号。该参数可选，如果该参数及SourceGroupOwnerID均未设置，则默认为同一账户安全组间授权。SourceCidrIp如果已经被设置，则该参数无效。
	private String SourceGroupOwnerID;// 可传。跨用户撤销安全组规则时，源安全组所属用户阿里云账号ID。该参数可选，如果该参数及SourceGroupOwnerAccount均未设置，则默认为同一账户安全组间撤销。SourceCidrIp如果已经被设置，则该参数无效。
	private String SourceCidrIp;// 可传。源IP地址范围，必须采用CIDR格式来指定IP地址范围，默认值为0.0.0.0/0（表示不受限制），其他支持的格式如10.159.6.18/12。仅支持IPV4。
	private String Policy;// 可传。授权策略，参数值可为：accept（接受访问），drop (拒绝访问)默认值为：accept
	private String Priority;// 可传。"授权策略优先级，参数值可为：1-100?默认值为：1"
	private String NicType;// 可传。"网络类型，取值：internet,intranet默认值为internet,当对安全组进行相互授权时（即指定了SourceGroupId且没有指定SourceCidrIp），必须指定NicType为intranet
	private String Direction;// 否 授权方向，取值：egress|ingress|all，默认值为all
}

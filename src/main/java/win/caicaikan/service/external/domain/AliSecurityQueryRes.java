/**
 * 
 */
package win.caicaikan.service.external.domain;

import java.util.List;

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
public class AliSecurityQueryRes {
	private String SecurityGroupId;// 目标安全组 Id
	private String SecurityGroupName;// 目标安全组名称
	private String RegionId;// 地域 Id
	private String Description;// 安全组描述信息
	private Permissions Permissions;// 由 PermissionType 组成的集合，表示安全组中的权限规则
	private String VpcId;// VpcId，如果有，表示这个安全组是 Vpc 类型的安全组，没有则表示是经典网络里的安全组。

	@Data
	@EqualsAndHashCode(callSuper = false)
	public static class Permissions {
		private List<Permission> Permission;
	}

	@Data
	@EqualsAndHashCode(callSuper = false)
	public static class Permission {
		private String IpProtocol;// IP协议
		private String PortRange;// 端口范围
		private String SourceCidrIp;// 源IP地址段，用于Ingress授权
		private String SourceGroupId;// 源安全组，用于Ingress授权
		private String SourceGroupOwnerAccount;// 源安全组所属阿里云账户Id
		private String DestCidrIp;// 目标IP地址段，用于Egress授权
		private String DestGroupId;// 目标安全组，用于Egress授权
		private String DestGroupOwnerAccount;// 目标安全组所属阿里云账户Id
		private String Policy;// 授权策略
		private String NicType;// 网络类型
		private String Priority;// 规则优先级
		private String Direction;// 授权方向
		private String Description;// 描述信息
	}

}

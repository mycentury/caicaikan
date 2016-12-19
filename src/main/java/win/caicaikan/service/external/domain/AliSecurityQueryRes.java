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
	private String RequestId;
	private String RegionId;
	private String SecurityGroupId;
	private String Description;
	private Permissions Permissions;

	@Data
	@EqualsAndHashCode(callSuper = false)
	public static class Permissions {
		private List<Permission> Permission;
	}

	@Data
	@EqualsAndHashCode(callSuper = false)
	public static class Permission {
		private String IpProtocol;// "ALL",
		private String PortRange;// "-1/-1",
		private String SourceGroupId;// "8dsmf982",
		private String SourceGroupOwnerAccount;// "test@aliyun.com"
		private String Policy;// "Accept",
		private String NicType;// "intranet"
	}

}

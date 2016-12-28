/**
 * 
 */
package win.caicaikan.repository.mongodb.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @Desc
 * @author wewenge.yan
 * @Date 2016年12月28日
 * @ClassName SysConfigEntity
 */
@Document(collection = "sys_config")
@Data
@EqualsAndHashCode(callSuper = false)
public class SysConfigEntity extends BaseEntity {
	private static final long serialVersionUID = 1L;
	/**
	 * sp-00-01
	 */
	@Id
	private String id;
	/**
	 * sp-系统参数
	 */
	private String type;
	/**
	 * 父编号
	 */
	private String parentNo;
	/**
	 * 子编号
	 */
	private String subNo;
	/**
	 * 参数名称
	 */
	private String name;
	/**
	 * 参数key
	 */
	private String key;
	/**
	 * 参数value
	 */
	private String value;
	/**
	 * 启用状态：1-启用，0-禁用
	 */
	private int status;

	public void setPrimaryKey(String type, String parentNo, String subNo) {
		this.type = type;
		this.parentNo = parentNo;
		this.subNo = subNo;
		this.id = type + "-" + parentNo + "-" + subNo;
	}
}

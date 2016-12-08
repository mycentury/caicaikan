/**
 * 
 */
package win.caicaikan.repository.mongodb.entity;

import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @Desc 菜单
 * @author wewenge.yan
 * @Date 2016年12月8日
 * @ClassName MenuEntity
 */
@Document(collection = "menu")
@Data
@EqualsAndHashCode(callSuper = false)
public class MenuEntity extends BaseEntity {
	private static final long serialVersionUID = 1L;
	/**
	 * type-level-no
	 */
	@Id
	private String id;
	/**
	 * 类型
	 */
	private String type;// H-header,B-bottom,C-content
	/**
	 * 等级
	 */
	private int level;
	/**
	 * 编号
	 */
	private String no;
	/**
	 * 中文名称
	 */
	private String nameZh;
	/**
	 * 英文名称
	 */
	private String nameEn;
	/**
	 * 地址，访问路径
	 */
	private String path;
	/**
	 * 启用状态：1-启用，0-禁用
	 */
	private int status = 1;
	/**
	 * 父菜单Id
	 */
	private List<MenuEntity> subMenus;

	public void setPrimaryKey(String type, int level, String no) {
		this.type = type;
		this.level = level;
		this.no = no;
		this.id = type + "-" + level + "-" + no;
	}
}

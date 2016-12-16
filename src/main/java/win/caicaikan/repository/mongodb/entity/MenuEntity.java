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
	 */
	@Id
	private String id;
	/**
	 * 顺序
	 */
	private int seq;
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
	 * 子菜单列表（封装用）
	 */
	private List<MenuEntity> subMenus;
}

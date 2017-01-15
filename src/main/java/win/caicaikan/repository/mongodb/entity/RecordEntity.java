/**
 * 
 */
package win.caicaikan.repository.mongodb.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @Desc 动作，用户操作记录
 * @author wewenge.yan
 * @Date 2016年12月13日
 * @ClassName LogEntity
 */
@Document(collection = "record")
@Data
@EqualsAndHashCode(callSuper = false)
public class RecordEntity extends BaseEntity {
	private static final long serialVersionUID = 1L;
	/**
	 * 自动生成
	 */
	@Id
	private String id;
	/**
	 * 操作类型
	 */
	private String opertype;
	/**
	 * 用户类型
	 */
	private String usertype;// A-admin,G-general
	/**
	 * 用户名称
	 */
	private String username;
	/**
	 * IP
	 */
	private String ip;
	/**
	 * 参数
	 */
	private String param;
	/**
	 * 内容修改前
	 */
	private String before;
	/**
	 * 内容修改后
	 */
	private String after;
}

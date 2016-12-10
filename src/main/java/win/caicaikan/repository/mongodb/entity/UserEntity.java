/**
 * 
 */
package win.caicaikan.repository.mongodb.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @desc 用户
 * @author yanwenge
 * @date 2016年12月10日
 * @class UserEntity
 */
@Document(collection = "user")
@Data
@EqualsAndHashCode(callSuper = false)
public class UserEntity extends BaseEntity {
	private static final long serialVersionUID = 1L;
	/**
	 * usertype-username
	 */
	@Id
	private String id;
	/**
	 * 用户类型
	 */
	private String usertype;// A-admin,G-general
	/**
	 * 用户名称
	 */
	private String username;
	/**
	 * 密码
	 */
	private String password;
	/**
	 * 等级
	 */
	private String level = "0";
	/**
	 * 登陆状态：1-已登录，0-未登录
	 */
	private String status = "0";

	public void setPrimaryKey(String usertype, String username) {
		this.usertype = usertype;
		this.username = username;
		this.id = usertype + "-" + username;
	}
}

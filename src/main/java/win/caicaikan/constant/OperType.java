/**
 * 
 */
package win.caicaikan.constant;

/**
 * @Desc
 * @author wewenge.yan
 * @Date 2016年12月13日
 * @ClassName OperType
 */
public enum OperType {
	REQUEST("一般请求"),
	PASSWORD("修改密码"),
	ATTACK("攻击"),
	LOGIN("登录"),
	REGISTER("注册"),
	AUTHORIZE("授权");
	private String desc;

	private OperType(String desc) {
		this.desc = desc;
	}

	public String getDesc() {
		return desc;
	}
}

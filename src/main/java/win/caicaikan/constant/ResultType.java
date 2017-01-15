/**
 * 
 */
package win.caicaikan.constant;

/**
 * @Desc
 * @author wewenge.yan
 * @Date 2016年12月23日
 * @ClassName ErrorType
 */
public enum ResultType {
	BLACK_LIST(110, "你已被加入黑名单"),
	// 成功
	SUCCESS(200, "操作成功"),
	// 权限错误
	NOT_LOGIN(300, "未登录不可操作！"),
	NO_AUTHORITY(301, "无权限不可操作！"),
	// 请求错误
	PARAMETER_ERROR(400, "请求参数错误！"),
	// 内部服务异常
	SERVICE_ERROR(500, "本服务异常！"),
	// 外部服务异常
	API_ERROR(600, "API服务异常！"),
	// 警告性，提示性错误
	USER_EXISTS(700, "用户已存在！");
	private int status;
	private String msg;

	public int getStatus() {
		return status;
	}

	public String getMsg() {
		return msg;
	}

	private ResultType(int status, String msg) {
		this.status = status;
		this.msg = msg;
	}
}

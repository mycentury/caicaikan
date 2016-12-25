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
	SUCCESS(0, "操作成功！"),
	PARAMETER_ERROR(400, "请求参数错误！"),
	SERVICE_ERROR(1000, "本服务异常！"),
	API_ERROR(2000, "API服务异常！"),
	NOT_LOGIN(410, "未登录不可操作！"),
	NO_AUTHORITY(420, "无权限操作！");
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

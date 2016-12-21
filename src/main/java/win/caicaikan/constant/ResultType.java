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
	SUCCESS(0, "success"),
	PARAMETER_ERROR(400, "请求参数错误！"),
	SERVICE_ERROR(1000, "本服务异常！"),
	API_ERROR(2000, "API服务异常！");
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

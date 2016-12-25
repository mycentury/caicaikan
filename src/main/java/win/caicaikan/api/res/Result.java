/**
 * 
 */
package win.caicaikan.api.res;

import java.io.Serializable;

import win.caicaikan.constant.ResultType;

/**
 * @Desc
 * @author wewenge.yan
 * @Date 2016年11月22日
 * @ClassName LotteryReq
 */
public class Result<T> implements Serializable {
	private static final long serialVersionUID = 1L;
	private int status = 0;
	private String message;
	private T data;

	public void setResultStatusAndMsg(ResultType resultType, String addition) {
		this.status = resultType.getStatus();
		this.message = resultType.getMsg() + (addition == null ? "" : "-->" + addition);
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}
}

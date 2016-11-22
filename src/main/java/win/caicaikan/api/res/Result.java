/**
 * 
 */
package win.caicaikan.api.res;

import java.io.Serializable;

/**
 * @Desc
 * @author wewenge.yan
 * @Date 2016年11月22日
 * @ClassName LotteryReq
 */
public class Result<T> implements Serializable {
	private static final long serialVersionUID = 1L;
	private int status;
	private String message;
	private T data;

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

/**
 * 
 */
package win.caicaikan.api.res;

import java.io.Serializable;

import org.springframework.util.StringUtils;

import win.caicaikan.constant.ResultType;

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

	public void setResultStatusAndMsg(ResultType resultType, String addition) {
		this.status = resultType.getStatus();
		if (StringUtils.isEmpty(resultType.getMsg())) {
			this.message = addition;
			return;
		}
		if (StringUtils.isEmpty(addition)) {
			this.message = resultType.getMsg();
			return;
		}
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

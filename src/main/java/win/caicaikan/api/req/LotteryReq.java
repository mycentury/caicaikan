/**
 * 
 */
package win.caicaikan.api.req;

import java.io.Serializable;

/**
 * @Desc
 * @author wewenge.yan
 * @Date 2016年11月22日
 * @ClassName LotteryReq
 */
public class LotteryReq implements Serializable {
	private static final long serialVersionUID = 1L;
	/**
	 * 彩票类型
	 */
	private String lotteryType;
	/**
	 * 查询类型（按日期range_date，按期号range）
	 */
	private String queryType;
	/**
	 * 起始期号
	 */
	private String start;
	/**
	 * 结束期号
	 */
	private String end;

	public String getLotteryType() {
		return lotteryType;
	}

	public void setLotteryType(String lotteryType) {
		this.lotteryType = lotteryType;
	}

	public String getQueryType() {
		return queryType;
	}

	public void setQueryType(String queryType) {
		this.queryType = queryType;
	}

	public String getStart() {
		return start;
	}

	public void setStart(String start) {
		this.start = start;
	}

	public String getEnd() {
		return end;
	}

	public void setEnd(String end) {
		this.end = end;
	}

}

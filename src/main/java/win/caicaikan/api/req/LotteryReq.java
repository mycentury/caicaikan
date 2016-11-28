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
	 * 查询类型（按日期range_date{"yyyy-MM-dd"}，按期号range{"yyyy+no"}）
	 */
	private String queryType;
	/**
	 * 查询期数
	 */
	private String count = "5";
	/**
	 * 起始期号(或日期)
	 */
	private String start;
	/**
	 * 结束期号(或日期)
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

	public String getCount() {
		return count;
	}

	public void setCount(String count) {
		this.count = count;
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

	@Override
	public String toString() {
		return "LotteryReq [lotteryType=" + lotteryType + ", queryType=" + queryType + ", count=" + count + ", start=" + start + ", end=" + end + "]";
	}
}

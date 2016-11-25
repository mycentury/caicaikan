/**
 * 
 */
package win.caicaikan.api.res;

import java.util.List;

import win.caicaikan.api.domain.LotteryCurrentBo;

/**
 * @Desc
 * @author wewenge.yan
 * @Date 2016年11月25日
 * @ClassName LotterySsqRes
 */
public class LotteryCurrentRes {
	private int rows;
	private String code;
	private String info;
	private List<LotteryCurrentBo> data;

	public int getRows() {
		return rows;
	}

	public void setRows(int rows) {
		this.rows = rows;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	public List<LotteryCurrentBo> getData() {
		return data;
	}

	public void setData(List<LotteryCurrentBo> data) {
		this.data = data;
	}

	@Override
	public String toString() {
		return "LotterySsqRes [rows=" + rows + ", code=" + code + ", info=" + info + ", data=" + data + "]";
	}
}

/**
 * 
 */
package win.caicaikan.api.domain;

/**
 * @Desc
 * @author wewenge.yan
 * @Date 2016年11月25日
 * @ClassName LotterySsqlBo
 */
public class LotteryCurrentBo extends BaseLottery {
	private static final long serialVersionUID = 1L;
	private String expect;
	private String opencode;
	private String opentime;
	private String opentimestamp;

	public String getExpect() {
		return expect;
	}

	public void setExpect(String expect) {
		this.expect = expect;
	}

	public String getOpencode() {
		return opencode;
	}

	public void setOpencode(String opencode) {
		this.opencode = opencode;
	}

	public String getOpentime() {
		return opentime;
	}

	public void setOpentime(String opentime) {
		this.opentime = opentime;
	}

	public String getOpentimestamp() {
		return opentimestamp;
	}

	public void setOpentimestamp(String opentimestamp) {
		this.opentimestamp = opentimestamp;
	}

	@Override
	public String toString() {
		return "LotterySsqlBo [expect=" + expect + ", opencode=" + opencode + ", opentime=" + opentime + ", opentimestamp=" + opentimestamp + "]";
	}
}

/**
 * 
 */
package win.caicaikan.constant;

/**
 * @Desc
 * @author wewenge.yan
 * @Date 2016年11月22日
 * @ClassName LotteryType
 */
public enum ExcuteStatusType {
	READY("准备"),
	RUNNING("执行中"),
	STOP("停止");

	private ExcuteStatusType(String desc) {
		this.desc = desc;
	}

	private String desc;

	public String getDesc() {
		return desc;
	}
}

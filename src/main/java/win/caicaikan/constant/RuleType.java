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
public enum RuleType {
	DISPLAY_TIMES("出次平衡"),
	DOUBLE_TIMES("连出平衡"),
	SKIP_TIMES("遗漏平衡"),
	GAUSSIAN_DISTRIBUTION("高斯分布"),
	MULTI("综合规则");

	private String desc;

	private RuleType(String desc) {
		this.desc = desc;
	}

	public String getDesc() {
		return desc;
	}
}

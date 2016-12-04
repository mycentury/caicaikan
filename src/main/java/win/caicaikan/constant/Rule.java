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
public enum Rule {
	SSQ_0_001("ssq", "0", "001", "出次平衡", "出现次数平均"),
	SSQ_0_002("ssq", "0", "002", "连出平衡", "连出次数平均"),
	SSQ_0_003("ssq", "0", "003", "遗漏平衡", "遗漏次数平均");

	private String lotteryType;
	private String ruleType;
	private String ruleNo;
	private String ruleName;
	private String ruleDesc;

	/**
	 * @param lotteryType
	 * @param ruleType
	 * @param ruleNo
	 * @param ruleName
	 * @param ruleDesc
	 */
	private Rule(String lotteryType, String ruleType, String ruleNo, String ruleName,
			String ruleDesc) {
		this.lotteryType = lotteryType;
		this.ruleType = ruleType;
		this.ruleNo = ruleNo;
		this.ruleName = ruleName;
		this.ruleDesc = ruleDesc;
	}

	public String getLotteryType() {
		return lotteryType;
	}

	public String getRuleType() {
		return ruleType;
	}

	public String getRuleNo() {
		return ruleNo;
	}

	public String getRuleName() {
		return ruleName;
	}

	public String getRuleDesc() {
		return ruleDesc;
	}
}

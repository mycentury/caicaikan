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
public enum LotteryType {
	SSQ("50", "ssq", "双色球");

	private LotteryType(String value, String code, String name) {
		this.value = value;
		this.code = code;
		this.name = name;
	}

	private String value;
	private String code;
	private String name;

	public String getValue() {
		return value;
	}

	public String getCode() {
		return code;
	}

	public String getName() {
		return name;
	}
}

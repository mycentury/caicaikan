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
public enum ExecuteStatus {
	REDAY("准备"), // 执行前状态，当任务比较多需分批时使用，如规则
	RUNNING("执行中"), // 执行中状态，不允许再次执行
	SUCCESS("成功"), // 成功状态，正常任务可再次执行
	FAILED("失败");// 失败状态，重试任务可再次执行

	private ExecuteStatus(String desc) {
		this.desc = desc;
	}

	private String desc;

	public String getDesc() {
		return desc;
	}
}

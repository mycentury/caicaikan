/**
 * 
 */
package win.caicaikan.constant;

/**
 * @Desc
 * @author wewenge.yan
 * @Date 2016年12月28日
 * @ClassName SysConfig
 */
public enum SysConfig {
	/**
	 * 双色球下期信息
	 */
	SSQ_NEXT_TERM("SP-00-00"),
	/**
	 * 双色球当前期信息
	 */
	SSQ_CURRENT_TERM("SP-00-01");
	private String id;

	private SysConfig(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}
}

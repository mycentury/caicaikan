/**
 * 
 */
package win.caicaikan.repository.mongodb.entity;

import java.util.Map;

import lombok.Data;
import lombok.EqualsAndHashCode;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @Desc 权值计算规则
 * @author wewenge.yan
 * @Date 2016年11月22日
 * @ClassName SsqLotteryEntity
 */
@Document(collection = "lottery_rule")
@Data
@EqualsAndHashCode(callSuper = false)
public class LotteryRuleEntity extends BaseEntity {
	private static final long serialVersionUID = 1L;
	/**
	 * lotteryType-ruleType-ruleNo
	 */
	@Id
	private String id;
	/**
	 * 彩种
	 */
	private String lotteryType;
	/**
	 * 规则类型：0-基础，1-衍生（复合）
	 */
	private String ruleType;
	/**
	 * 规则编号
	 */
	private String ruleNo;
	/**
	 * 规则名称
	 */
	private String ruleName;
	/**
	 * 规则描述
	 */
	private String ruleDesc;
	/**
	 * 期数和权值
	 */
	Map<Integer, Integer> periodsAndweights;
	/**
	 * 启用状态：1-启用，0-禁用
	 */
	private int status;
	/**
	 * 红球选取个数
	 */
	private int redCount;
	/**
	 * 启用状态：1-启用，0-禁用
	 */
	private int blueCount;
	/**
	 * 执行状态
	 */
	private String excuteStatus;

	public void setPrimaryKey(String lotteryType, String ruleType, String ruleNo) {
		this.lotteryType = lotteryType;
		this.ruleType = ruleType;
		this.ruleNo = ruleNo;
		this.id = lotteryType + "-" + ruleType + "-" + ruleNo;
	}
}

/**
 * 
 */
package win.caicaikan.repository.mongodb.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @Desc 权值计算规则
 * @author wewenge.yan
 * @Date 2016年11月22日
 * @ClassName PredictRuleEntity
 */
@Document(collection = "predict_rule")
@Data
@EqualsAndHashCode(callSuper = false)
public class PredictRuleEntity extends BaseEntity {
	private static final long serialVersionUID = 1L;
	/**
	 * lotteryType-ruleType-terms
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
	 * 期数
	 */
	private int terms;
	/**
	 * 规则名称
	 */
	private String ruleName;
	/**
	 * 基于的规则和权值
	 */
	private String ruleAndweights;
	/**
	 * 启用状态：1-启用，0-禁用
	 */
	private int status;
	/**
	 * 执行状态
	 */
	private String excuteStatus;

	public void setPrimaryKey(String lotteryType, String ruleType, int terms) {
		this.lotteryType = lotteryType;
		this.ruleType = ruleType;
		this.terms = terms;
		this.id = lotteryType + "-" + ruleType + "-" + terms;
	}
}

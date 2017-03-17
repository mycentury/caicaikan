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
@Document(collection = "mock_rule")
@Data
@EqualsAndHashCode(callSuper = false)
public class MockRuleEntity extends BaseEntity {
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
	private Double firstRate;// 一等奖准确率
	private Double secondRate;// 二等奖准确率
	private Double firstRedRate;// 1号球准确率
	private Double secondRedRate;// 2号球准确率
	private Double thirdRedRate;// 3号球准确率
	private Double forthRedRate;// 4号球准确率
	private Double fifthRedRate;// 5号球准确率
	private Double sixthRedRate;// 6号球准确率
	private Double blueRate;// 蓝球准确率
	private Double multipleRedRate;// 综合准确率
	private Double multipleBlueRate;// 综合准确率
	/**
	 * 启用状态：1-启用，0-禁用
	 */
	private int status;
	/**
	 * 执行状态
	 */
	private String executeStatus;

	public void setPrimaryKey(String lotteryType, String ruleType, int terms) {
		this.lotteryType = lotteryType;
		this.ruleType = ruleType;
		this.terms = terms;
		this.id = lotteryType + "-" + ruleType + "-" + terms;
	}
}

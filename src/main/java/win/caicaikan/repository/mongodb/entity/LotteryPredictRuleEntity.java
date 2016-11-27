/**
 * 
 */
package win.caicaikan.repository.mongodb.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @Desc
 * @author wewenge.yan
 * @Date 2016年11月22日
 * @ClassName SsqLotteryEntity
 */
@Document(collection = "lottery_predict_rule")
@Data
@EqualsAndHashCode(callSuper = false)
public class LotteryPredictRuleEntity extends BaseEntity {
	private static final long serialVersionUID = 1L;
	@Id
	private String id;// 彩种+规则编号
	private String lotteryType;// 彩种
	private String ruleNo;// 规则编号
	private String numbers;
	private String rightNumbers;
}

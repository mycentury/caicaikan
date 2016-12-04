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
@Document(collection = "lottery_predict")
@Data
@EqualsAndHashCode(callSuper = false)
public class LotteryPredictEntity extends BaseEntity {
	private static final long serialVersionUID = 1L;
	@Id
	private String id;// lotteryType-ruleNo-termNo
	private String lotteryType;// 彩种
	private String ruleNo;
	private String termNo;
	private String numbers;
	private String rightNumbers;

	public void setPrimaryKey(String lotteryType, String ruleNo, String termNo) {
		this.lotteryType = lotteryType;
		this.ruleNo = ruleNo;
		this.termNo = termNo;
		this.id = lotteryType + "-" + ruleNo + "-" + termNo;
	}
}

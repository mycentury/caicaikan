/**
 * 
 */
package win.caicaikan.repository.mongodb.entity;

import java.math.BigInteger;

import lombok.Data;
import lombok.EqualsAndHashCode;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @Desc
 * @author wewenge.yan
 * @Date 2016年11月22日
 * @ClassName SsqResultEntity
 */
@Document(collection = "prize_rule")
@Data
@EqualsAndHashCode(callSuper = false)
public class PrizeRuleEntity extends BaseEntity {
	private static final long serialVersionUID = 1L;
	/**
	 * lotteryType-level
	 */
	@Id
	private String id;
	private String lotteryType;
	private String level;
	private String condition;
	private BigInteger prizeCases;
	private BigInteger allCases;
	private BigInteger rate;
	private String name;
	private String desc;

	public void setPrimaryKey(String lotteryType, String level) {
		this.lotteryType = lotteryType;
		this.level = level;
		this.id = lotteryType + "-" + level;
	}

	public void setRate(BigInteger prizeCases, BigInteger allCases) {
		this.prizeCases = prizeCases;
		this.allCases = allCases;
		this.rate = allCases.divide(prizeCases);
	}
}

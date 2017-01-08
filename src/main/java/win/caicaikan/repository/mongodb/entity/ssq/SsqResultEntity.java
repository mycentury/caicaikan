/**
 * 
 */
package win.caicaikan.repository.mongodb.entity.ssq;

import lombok.Data;
import lombok.EqualsAndHashCode;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import win.caicaikan.repository.mongodb.entity.BaseEntity;

/**
 * @Desc
 * @author wewenge.yan
 * @Date 2016年11月22日
 * @ClassName SsqResultEntity
 */
@Document(collection = "ssq_result")
@Data
@EqualsAndHashCode(callSuper = false)
public class SsqResultEntity extends BaseEntity {
	private static final long serialVersionUID = 1L;
	@Id
	private String termNo;
	private String openTime;
	private String redNumbers;
	private String blueNumbers;
	private String firstPrizeCount;
	private String firstPrizeAmount;
	private String secondPrizeCount;
	private String secondPrizeAmount;

	@Override
	public String getId() {
		return termNo;
	}
}

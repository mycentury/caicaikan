/**
 * 
 */
package win.caicaikan.repository.mongodb.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @Desc
 * @author wewenge.yan
 * @Date 2016年11月22日
 * @ClassName SsqLotteryEntity
 */
@Document(collection = "lottery_ssq")
public class LotterySsqEntity extends BaseEntity {
	private static final long serialVersionUID = 1L;
	@Id
	private String termNo;
	private String openTime;
	private String redNumbers;
	private String blueNumber;
	private String firstPrizeCount;
	private String firstPrizeAmount;
	private String secondPrizeCount;
	private String secondPrizeAmount;

	public String getTermNo() {
		return termNo;
	}

	public void setTermNo(String termNo) {
		this.termNo = termNo;
	}

	public String getOpenTime() {
		return openTime;
	}

	public void setOpenTime(String openTime) {
		this.openTime = openTime;
	}

	public String getRedNumbers() {
		return redNumbers;
	}

	public void setRedNumbers(String redNumbers) {
		this.redNumbers = redNumbers;
	}

	public String getBlueNumber() {
		return blueNumber;
	}

	public void setBlueNumber(String blueNumber) {
		this.blueNumber = blueNumber;
	}

	public String getFirstPrizeCount() {
		return firstPrizeCount;
	}

	public void setFirstPrizeCount(String firstPrizeCount) {
		this.firstPrizeCount = firstPrizeCount;
	}

	public String getFirstPrizeAmount() {
		return firstPrizeAmount;
	}

	public void setFirstPrizeAmount(String firstPrizeAmount) {
		this.firstPrizeAmount = firstPrizeAmount;
	}

	public String getSecondPrizeCount() {
		return secondPrizeCount;
	}

	public void setSecondPrizeCount(String secondPrizeCount) {
		this.secondPrizeCount = secondPrizeCount;
	}

	public String getSecondPrizeAmount() {
		return secondPrizeAmount;
	}

	public void setSecondPrizeAmount(String secondPrizeAmount) {
		this.secondPrizeAmount = secondPrizeAmount;
	}

	@Override
	public String toString() {
		return "LotterySsqEntity [termNo=" + termNo + ", openTime=" + openTime + ", redNumbers=" + redNumbers + ", blueNumber=" + blueNumber
				+ ", firstPrizeCount=" + firstPrizeCount + ", firstPrizeAmount=" + firstPrizeAmount + ", secondPrizeCount=" + secondPrizeCount
				+ ", secondPrizeAmount=" + secondPrizeAmount + ", toString()=" + super.toString() + "]";
	}
}

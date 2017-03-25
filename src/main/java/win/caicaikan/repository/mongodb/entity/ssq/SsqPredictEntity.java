/**
 * 
 */
package win.caicaikan.repository.mongodb.entity.ssq;

import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import win.caicaikan.repository.mongodb.entity.BaseEntity;

/**
 * @Desc
 * @author wewenge.yan
 * @Date 2016年11月22日
 * @ClassName PredictEntity
 */
@Document(collection = "ssq_predict")
@Data
@EqualsAndHashCode(callSuper = false)
public class SsqPredictEntity extends BaseEntity {
	private static final long serialVersionUID = 1L;
	@Id
	private String id;// ruleId-termNo
	private String ruleId;
	private String termNo;
	private List<String> redNumbers;
	private List<String> blueNumbers;
	private List<String> redGsNumbers;
	private String rightNumbers;

	public void setPrimaryKey(String ruleNo, String termNo) {
		this.ruleId = ruleNo;
		this.termNo = termNo;
		this.id = ruleNo + "-" + termNo;
	}
}

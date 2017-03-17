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
@Document(collection = "mock_recommend")
@Data
@EqualsAndHashCode(callSuper = false)
public class MockRecommendEntity extends BaseEntity {
	private static final long serialVersionUID = 1L;
	@Id
	private String id;// Ssq-termNo
	private String recommondId;
	private String termNo;
	private String recommendNumbers;
	private List<String> redNumbers;
	private List<String> blueNumbers;

	public void setPrimaryKey(String recommondId, String termNo) {
		this.recommondId = recommondId;
		this.termNo = termNo;
		this.id = recommondId + "-" + termNo;
	}
}

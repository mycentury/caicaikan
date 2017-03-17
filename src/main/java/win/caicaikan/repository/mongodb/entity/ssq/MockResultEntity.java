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
@Document(collection = "mock_result")
@Data
@EqualsAndHashCode(callSuper = false)
public class MockResultEntity extends BaseEntity {
	private static final long serialVersionUID = 1L;
	@Id
	private String id;
	private String reds;
	private String blue;
}

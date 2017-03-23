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
@Document(collection = "gs_count")
@Data
@EqualsAndHashCode(callSuper = false)
public class GsCountEntity extends BaseEntity {
	private static final long serialVersionUID = 1L;
	@Id
	private String id;
	private String type;
	private Integer key;
	private Integer rate;
	private Integer count;

	public void setPrimaryKey(String type, Integer key) {
		this.type = type;
		this.key = key;
		this.id = type + "-" + key;
	}
}

package win.caicaikan.repository.mongodb.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "task")
@Data
@EqualsAndHashCode(callSuper = false)
public class TaskEntity extends BaseEntity {
	private static final long serialVersionUID = 1L;
	@Id
	private String id;// TaskName
	private Object param;// 传入参数
	private String info;// 错误信息等
	private String executeStatus;// 运行状态
}

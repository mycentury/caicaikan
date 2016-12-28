/**
 * 
 */
package win.caicaikan.repository.mongodb.entity;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;
import lombok.EqualsAndHashCode;

import org.springframework.format.annotation.DateTimeFormat;

/**
 * @Desc
 * @author wewenge.yan
 * @Date 2016年11月22日
 * @ClassName BaseEntity
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date updateTime;
	private Date createTime;

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	@Override
	public String toString() {
		return "BaseEntity [updateTime=" + updateTime + ", createTime=" + createTime + "]";
	}
}

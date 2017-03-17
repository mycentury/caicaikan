/**
 * 
 */
package win.caicaikan.service.internal;

import java.util.Date;

import win.caicaikan.repository.mongodb.entity.TaskEntity;
import win.caicaikan.util.GsonUtil;

/**
 * @Desc
 * @author wewenge.yan
 * @Date 2017年3月17日
 * @ClassName Test
 */
public class Test {
	public static void main(String[] args) {
		TaskEntity entity = new TaskEntity();
		entity.setId("id");
		entity.setCreateTime(new Date());
		entity.setUpdateTime(new Date());
		String json = GsonUtil.toJson(entity);
		System.out.println(json);
		TaskEntity fromJson = GsonUtil.fromJson(json, TaskEntity.class);
		System.out.println(fromJson.getCreateTime());
	}
}

/**
 * 
 */
package win.caicaikan.service.internal;

import java.util.Date;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import win.caicaikan.BaseTest;
import win.caicaikan.constant.ExecuteStatus;
import win.caicaikan.repository.mongodb.entity.SysConfigEntity;
import win.caicaikan.repository.mongodb.entity.TaskEntity;

/**
 * @Desc
 * @author wewenge.yan
 * @Date 2016年12月28日
 * @ClassName DaoServiceTest
 */
public class DaoServiceTest extends BaseTest {
	@Autowired
	private DaoService daoService;

	@Test
	public void testInsertSysConfig() {
		SysConfigEntity entity = new SysConfigEntity();
		entity.setPrimaryKey("SP", "00", "00");
		entity.setName("双色球下期期号");
		entity.setKey("2016-12-27 21:20:40");
		entity.setValue("2016152");
		entity.setStatus(1);
		entity.setCreateTime(new Date());
		entity.setUpdateTime(new Date());
		daoService.insert(entity);
	}

	@Test
	public void testInsertTasks() {
		TaskEntity entity = new TaskEntity();
		entity.setId("SsqCurrentTask");
		entity.setExecuteStatus(ExecuteStatus.FAILED.name());
		daoService.insert(entity);

		entity = new TaskEntity();
		entity.setId("SsqHistoryTask");
		entity.setExecuteStatus(ExecuteStatus.FAILED.name());
		daoService.insert(entity);

		entity = new TaskEntity();
		entity.setId("SsqPredictTask");
		entity.setExecuteStatus(ExecuteStatus.FAILED.name());
		daoService.insert(entity);
	}
}

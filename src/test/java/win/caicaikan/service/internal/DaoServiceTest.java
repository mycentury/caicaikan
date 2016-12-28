/**
 * 
 */
package win.caicaikan.service.internal;

import java.util.Date;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import win.caicaikan.BaseTest;
import win.caicaikan.repository.mongodb.entity.SysConfigEntity;

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
}

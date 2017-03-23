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
 * @desc
 * @author yanwenge
 * @date 2017年1月8日
 * @class InitServiceTest
 */
public class InitServiceTest extends BaseTest {

	@Autowired
	private InitService initService;
	@Autowired
	private DaoService daoService;

	/**
	 * Test method for {@link InitService#initSsqResults()}.
	 */
	@Test
	public void testInitSsqResults() {
		initService.initSsqResults();
	}

	/**
	 * Test method for {@link InitService#initSsqGsParam()}.
	 */
	@Test
	public void testInitSsqGsParam() {
		initService.initSsqGsParam();
	}

	/**
	 * Test method for {@link InitService#initSsqPredicts(int)}.
	 */
	@Test
	public void testInitSsqPredicts() {
		initService.initSsqPredicts(1000);
	}

	/**
	 * Test method for {@link InitService#initPredictRightPositions()} .
	 */
	@Test
	public void testInitPredictRightPositions() {
		initService.initPredictRightPositions();
	}

	/**
	 * Test method for {@link InitService#initMenus()}.
	 */
	@Test
	public void testInitMenus() {
		initService.initMenus();
	}

	/**
	 * Test method for {@link InitService#initSysConfigs()}.
	 */
	@Test
	public void testInitSysConfigs() {
		initService.initSysConfigs();
	}

	@Test
	public void testInitSysConfigs2() {
		SysConfigEntity entity = new SysConfigEntity();
		entity.setPrimaryKey("SP", "00", "02");
		entity.setName("红球总数高斯分布参数");
		entity.setStatus(1);
		entity.setCreateTime(new Date());
		entity.setUpdateTime(new Date());
		daoService.save(entity);
	}

	/**
	 * Test method for {@link InitService#initTasks()}.
	 */
	@Test
	public void testInitTasks() {
		initService.initTasks();
	}

	/**
	 * Test method for {@link InitService#initPredictRules()}.
	 */
	@Test
	public void testInitPredictRules() {
		initService.initPredictRules();
	}

	/**
	 * Test method for {@link InitService#initSsqPrizeRules()}.
	 */
	@Test
	public void testInitSsqPrizeRules() {
		initService.initSsqPrizeRules();
	}

}

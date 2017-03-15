/**
 * 
 */
package win.caicaikan.service.internal;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import win.caicaikan.BaseTest;

/**
 * @desc
 * @author yanwenge
 * @date 2017年1月8日
 * @class InitServiceTest
 */
public class InitServiceTest extends BaseTest {

	@Autowired
	private InitService initService;

	/**
	 * Test method for {@link InitService#initSsqResults()}.
	 */
	@Test
	public void testInitSsqResults() {
		initService.initSsqResults();
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

/**
 * 
 */
package win.caicaikan.service.internal;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import win.caicaikan.BaseTest;

/**
 * @Desc
 * @author wewenge.yan
 * @Date 2016年12月28日
 * @ClassName DaoServiceTest
 */
public class DaoServiceTest extends BaseTest {
	@Autowired
	private DaoService daoService;
	@Autowired
	private InitService initService;

	@Test
	public void testInitSysConfigs() {
		initService.initSysConfigs();
	}

	@Test
	public void testInitMenus() {
		initService.initMenus();
	}

	@Test
	public void testInitTasks() {
		initService.initTasks();
	}

	@Test
	public void testInitPredictRules() {
		initService.initPredictRules();
	}

	@Test
	public void testInitSsqPrizeRules() {
		initService.initSsqPrizeRules();
	}
}

/**
 * 
 */
package win.caicaikan.service.internal;

import static org.junit.Assert.*;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import win.caicaikan.BaseTest;

/**
 * @Desc
 * @author wewenge.yan
 * @Date 2017年3月22日
 * @ClassName CalculateServiceTest
 */
public class CalculateServiceTest extends BaseTest {

	@Autowired
	private CalculateService calculateService;

	/**
	 * Test method for {@link win.caicaikan.service.internal.CalculateService#initPrizeRuleMap()}.
	 */
	@Test
	public void testInitPrizeRuleMap() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link win.caicaikan.service.internal.CalculateService#countRate()}.
	 */
	@Test
	public void testCountRate() {
		calculateService.countRate();
	}

	/**
	 * Test method for
	 * {@link win.caicaikan.service.internal.CalculateService#countRate(win.caicaikan.repository.mongodb.entity.PredictRuleEntity, int, int)}
	 * .
	 */
	@Test
	public void testCountRatePredictRuleEntityIntInt() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link win.caicaikan.service.internal.CalculateService#afterPropertiesSet()}.
	 */
	@Test
	public void testAfterPropertiesSet() {
		fail("Not yet implemented");
	}

}

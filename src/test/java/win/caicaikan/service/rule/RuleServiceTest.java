/**
 * 
 */
package win.caicaikan.service.rule;

import static org.junit.Assert.fail;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import win.caicaikan.BaseTest;

/**
 * @Desc
 * @author wewenge.yan
 * @Date 2017年3月15日
 * @ClassName RuleServiceTest
 */
public class RuleServiceTest extends BaseTest {

	@Autowired
	private RuleService ruleService;

	/**
	 * Test method for {@link win.caicaikan.service.rule.RuleService#countSsqAllCases()}.
	 */
	@Test
	public void testCountSsqAllCases() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link win.caicaikan.service.rule.RuleService#countSsqPredictCases()}.
	 */
	@Test
	public void testCountSsqPredictCases() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link win.caicaikan.service.rule.RuleService#countSsqPrizeCases(java.lang.String)}.
	 */
	@Test
	public void testCountSsqPrizeCases() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link win.caicaikan.service.rule.RuleService#getCurrentTermNoOfSsq()}.
	 */
	@Test
	public void testGetCurrentTermNoOfSsq() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link win.caicaikan.service.rule.RuleService#getNextTermNoOfSsq()}.
	 */
	@Test
	public void testGetNextTermNoOfSsq() {
		String nextTermNoOfSsq = ruleService.getNextTermNoOfSsq();
		System.out.println(nextTermNoOfSsq);
	}

	/**
	 * Test method for
	 * {@link win.caicaikan.servicegetNextTermNoOfSsqe#getNextTermOfSsq(win.caicaikan.repository.mongodb.entity.ssq.SsqResultEntity)}
	 * .
	 */
	@Test
	public void testGetNextTermNoOfSsqSsqResultEntity() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link win.caicaikan.service.rule.RuleService#getNextTermOpenDateOfSsq(java.lang.String)}.
	 */
	@Test
	public void testGetNextTermOpenDateOfSsq() {
		fail("Not yet implemented");
	}

}

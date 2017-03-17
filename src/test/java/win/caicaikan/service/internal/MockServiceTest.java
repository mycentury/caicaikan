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
 * @Date 2017年3月17日
 * @ClassName MockServiceTest
 */
public class MockServiceTest extends BaseTest {
	@Autowired
	private MockService mockService;

	/**
	 * Test method for {@link win.caicaikan.service.internal.MockService#initMockResults()}.
	 */
	@Test
	public void testInitMockResults() {
		mockService.initMockResults();
	}

	/**
	 * Test method for {@link win.caicaikan.service.internal.MockService#initMockRules()}.
	 */
	@Test
	public void testInitMockRules() {
		mockService.initMockRules();
	}

	/**
	 * Test method for {@link win.caicaikan.service.internal.MockService#initMockPredicts()}.
	 */
	@Test
	public void testInitMockPredicts() {
		mockService.initMockPredicts();
	}

}

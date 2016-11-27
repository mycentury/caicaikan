/**
 * 
 */
package win.caicaikan.task;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import win.caicaikan.BaseTest;

/**
 * @author yanwenge
 *
 */
public class LotteryPredictTaskTest extends BaseTest {

	@Autowired
	private LotteryPredictTask lotteryPredictTask;

	/**
	 * Test method for {@link win.caicaikan.task.LotteryPredictTask#execute()}.
	 */
	@Test
	public void testExecute() {
		lotteryPredictTask.execute();
	}

}

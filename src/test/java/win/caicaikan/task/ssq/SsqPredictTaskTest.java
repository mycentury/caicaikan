/**
 * 
 */
package win.caicaikan.task.ssq;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import win.caicaikan.BaseTest;
import win.caicaikan.task.ssq.SsqPredictTask;

/**
 * @author yanwenge
 *
 */
public class SsqPredictTaskTest extends BaseTest {

	@Autowired
	private SsqPredictTask ssqPredictTask;

	/**
	 * Test method for {@link win.caicaikan.task.ssq.SsqPredictTask#execute()}.
	 */
	@Test
	public void testExecute() {
		ssqPredictTask.execute();
	}

}

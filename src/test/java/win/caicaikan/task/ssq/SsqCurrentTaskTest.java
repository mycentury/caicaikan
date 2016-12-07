/**
 * 
 */
package win.caicaikan.task.ssq;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import win.caicaikan.BaseTest;
import win.caicaikan.task.ssq.SsqCurrentTask;

/**
 * @Desc
 * @author wewenge.yan
 * @Date 2016年11月25日
 * @ClassName LotterySsqCurrentTaskTest
 */
public class SsqCurrentTaskTest extends BaseTest {

	@Autowired
	private SsqCurrentTask ssqCurrentTask;

	/**
	 * Test method for {@link win.caicaikan.task.ssq.SsqCurrentTask#execute()}.
	 */
	@Test
	public void testExecute() {
		ssqCurrentTask.execute();
	}

}

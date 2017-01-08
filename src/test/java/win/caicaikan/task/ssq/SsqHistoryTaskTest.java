/**
 * 
 */
package win.caicaikan.task.ssq;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import win.caicaikan.BaseTest;

/**
 * @Desc
 * @author wewenge.yan
 * @Date 2016年11月22日
 * @ClassName LotterySsqTaskTest
 */
public class SsqHistoryTaskTest extends BaseTest {

	@Autowired
	private SsqHistoryTask ssqHistoryTask;

	@Test
	public void testExecute() {
		ssqHistoryTask.execute();
	}
}

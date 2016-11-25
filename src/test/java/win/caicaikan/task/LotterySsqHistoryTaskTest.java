/**
 * 
 */
package win.caicaikan.task;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import win.caicaikan.BaseTest;

/**
 * @Desc
 * @author wewenge.yan
 * @Date 2016年11月22日
 * @ClassName LotterySsqTaskTest
 */
public class LotterySsqHistoryTaskTest extends BaseTest {

	@Autowired
	private LotterySsqHistoryTask lotterySsqHistoryTask;

	@Test
	public void testExecute() {
		lotterySsqHistoryTask.execute();
	}

	@Test
	public void testSynchronizeHistoryData() {
		lotterySsqHistoryTask.synchronizeHistoryData();
	}

}

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
 * @Date 2016年11月25日
 * @ClassName LotterySsqCurrentTaskTest
 */
public class LotterySsqCurrentTaskTest extends BaseTest {

	@Autowired
	private LotterySsqCurrentTask task;

	/**
	 * Test method for {@link win.caicaikan.task.LotterySsqCurrentTask#execute()}.
	 */
	@Test
	public void testExecute() {
		task.execute();
	}

}

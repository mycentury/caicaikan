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
public class LotterySsqTaskTest extends BaseTest {

	@Autowired
	private LotterySsqTask lotterySsqTask;

	@Test
	public void testExecute() {
		lotterySsqTask.execute();
	}

}

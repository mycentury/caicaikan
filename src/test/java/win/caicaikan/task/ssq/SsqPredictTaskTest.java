/**
 * 
 */
package win.caicaikan.task.ssq;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;

import win.caicaikan.BaseTest;
import win.caicaikan.repository.mongodb.dao.ssq.SsqResultDao;
import win.caicaikan.repository.mongodb.entity.ssq.SsqResultEntity;

/**
 * @author yanwenge
 */
public class SsqPredictTaskTest extends BaseTest {

	@Autowired
	private SsqPredictTask ssqPredictTask;
	@Autowired
	private SsqResultDao ssqResultDao;

	/**
	 * Test method for {@link win.caicaikan.task.ssq.SsqPredictTask#execute()}.
	 */
	@Test
	public void testExecute() {
		ssqPredictTask.execute();
	}

	/**
	 * Test method for {@link win.caicaikan.task.ssq.SsqPredictTask#predictByResults(java.util.List)} .
	 */
	@Test
	public void testPredictByResults() {
		Sort sort = new Sort(new Sort.Order(Sort.Direction.DESC, "termNo"));
		List<SsqResultEntity> list = ssqResultDao.findAll(sort);
		for (int i = 0; i < 100; i++) {
			try {
				if (i >= 1) {
					list.remove(0);
				}
				ssqPredictTask.predictByResults(list);
			} catch (Throwable e) {
				e.printStackTrace();
			}
		}
	}

}

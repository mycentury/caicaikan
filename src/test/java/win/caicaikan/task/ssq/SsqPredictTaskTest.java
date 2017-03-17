/**
 * 
 */
package win.caicaikan.task.ssq;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort.Direction;

import win.caicaikan.BaseTest;
import win.caicaikan.repository.mongodb.entity.PredictRuleEntity;
import win.caicaikan.repository.mongodb.entity.ssq.SsqResultEntity;
import win.caicaikan.service.internal.DaoService;
import win.caicaikan.service.internal.DaoService.Condition;

/**
 * @author yanwenge
 */
public class SsqPredictTaskTest extends BaseTest {

	@Autowired
	private DaoService daoService;

	@Autowired
	private SsqPredictTask ssqPredictTask;

	/**
	 * Test method for {@link win.caicaikan.task.ssq.SsqPredictTask#execute()}.
	 */
	@Test
	public void testExecute() {
		ssqPredictTask.execute();
	}

	/**
	 * Test method for {@link win.caicaikan.task.ssq.SsqPredictTask#recommendBest(java.util.List, String, int, int)}.
	 */
	@Test
	public void testRecommendBest() {
		Condition condition = new Condition();
		condition.setOrder(Direction.DESC);
		condition.setOrderBy(daoService.getIdColName(SsqResultEntity.class));
		condition.setLimit(1000);
		List<SsqResultEntity> results = daoService.query(condition, SsqResultEntity.class);
		List<PredictRuleEntity> rules = daoService.query(null, PredictRuleEntity.class);
		for (SsqResultEntity result : results) {
			ssqPredictTask.recommendBest(rules, result.getId(), 10, 5);
		}
	}
}

/**
 * 
 */
package win.caicaikan.repository.mongodb.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import win.caicaikan.BaseTest;
import win.caicaikan.constant.ExcuteStatus;
import win.caicaikan.constant.LotteryType;
import win.caicaikan.constant.RuleType;
import win.caicaikan.constant.StatusType;
import win.caicaikan.repository.mongodb.entity.PredictRuleEntity;

/**
 * @author yanwenge
 */
public class PredictRuleDaoTest extends BaseTest {

	@Autowired
	private PredictRuleDao predictRuleDao;

	/**
	 * Test method for {@link org.springframework.data.mongodb.repository.MongoRepository#insert(java.lang.Iterable)} .
	 */
	@Test
	public void testInsertIterableOfS() {
		PredictRuleEntity entity = null;
		Map<Integer, Integer> map = null;
		List<PredictRuleEntity> list = new ArrayList<PredictRuleEntity>();
		// SSQ-DISPLAY_TIMES-2000
		entity = new PredictRuleEntity();
		entity.setPrimaryKey(LotteryType.SSQ.getCode(), RuleType.DISPLAY_TIMES.name(), 2000);
		entity.setRuleName("出次均衡");
		entity.setTerms(2000);
		entity.setStatus(StatusType.ACTIVE.getCode());
		entity.setExcuteStatus(ExcuteStatus.STOP.name());
		entity.setCreateTime(new Date());
		entity.setUpdateTime(new Date());
		list.add(entity);

		// SSQ-SKIP_TIMES-2000
		entity = new PredictRuleEntity();
		entity.setPrimaryKey(LotteryType.SSQ.getCode(), RuleType.SKIP_TIMES.name(), 2000);
		entity.setRuleName("遗漏均衡");
		entity.setTerms(2000);
		entity.setStatus(StatusType.ACTIVE.getCode());
		entity.setExcuteStatus(ExcuteStatus.STOP.name());
		entity.setCreateTime(new Date());
		entity.setUpdateTime(new Date());
		list.add(entity);

		// SSQ-DOUBLE_TIMES-2000
		entity = new PredictRuleEntity();
		entity.setPrimaryKey(LotteryType.SSQ.getCode(), RuleType.DOUBLE_TIMES.name(), 2000);
		entity.setRuleName("连出均衡");
		entity.setTerms(2000);
		entity.setStatus(StatusType.INACTIVE.getCode());
		entity.setExcuteStatus(ExcuteStatus.STOP.name());
		entity.setCreateTime(new Date());
		entity.setUpdateTime(new Date());
		list.add(entity);

		predictRuleDao.save(list);
	}
}

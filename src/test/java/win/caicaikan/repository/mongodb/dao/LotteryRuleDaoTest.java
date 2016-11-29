/**
 * 
 */
package win.caicaikan.repository.mongodb.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import win.caicaikan.BaseTest;
import win.caicaikan.constant.ExcuteStatusType;
import win.caicaikan.constant.LotteryType;
import win.caicaikan.constant.RuleType;
import win.caicaikan.constant.StatusType;
import win.caicaikan.repository.mongodb.entity.LotteryRuleEntity;

/**
 * @author yanwenge
 */
public class LotteryRuleDaoTest extends BaseTest {

	@Autowired
	private LotteryRuleDao dao;

	/**
	 * Test method for {@link org.springframework.data.mongodb.repository.MongoRepository#insert(java.lang.Iterable)}.
	 */
	@Test
	public void testInsertIterableOfS() {
		LotteryRuleEntity entity = null;
		Map<Integer, Integer> map = null;
		List<LotteryRuleEntity> list = new ArrayList<LotteryRuleEntity>();
		// SSQ-0-001
		entity = new LotteryRuleEntity();
		entity.setLotteryType(LotteryType.SSQ.getCode());
		entity.setRuleType(RuleType.BASE.getCode());
		entity.setRuleNo("001");
		entity.generateId();
		entity.setRuleName("出次均衡");
		entity.setStatus(StatusType.INACTIVE.getCode());
		entity.setRuleDesc("根据权重map统计出现次数");
		map = new HashMap<Integer, Integer>();
		map.put(0, 70);
		map.put(1000, 16);
		map.put(500, 8);
		map.put(200, 4);
		map.put(100, 2);
		entity.setPeriodsAndweights(map);
		entity.setExcuteStatus(ExcuteStatusType.STOP.name());
		entity.setCreateTime(new Date());
		entity.setUpdateTime(new Date());
		list.add(entity);

		// SSQ-0-002
		entity = new LotteryRuleEntity();
		entity.setLotteryType(LotteryType.SSQ.getCode());
		entity.setRuleType(RuleType.BASE.getCode());
		entity.setRuleNo("002");
		entity.generateId();
		entity.setRuleName("连出均衡");
		entity.setStatus(StatusType.INACTIVE.getCode());
		entity.setRuleDesc("根据权重map统计连出次数");
		map = new HashMap<Integer, Integer>();
		map.put(0, -25);
		map.put(1000, -10);
		map.put(500, -5);
		map.put(200, -2);
		map.put(100, -1);
		entity.setPeriodsAndweights(map);
		entity.setExcuteStatus(ExcuteStatusType.STOP.name());
		entity.setCreateTime(new Date());
		entity.setUpdateTime(new Date());
		list.add(entity);

		// SSQ-0-003
		entity = new LotteryRuleEntity();
		entity.setLotteryType(LotteryType.SSQ.getCode());
		entity.setRuleType(RuleType.BASE.getCode());
		entity.setRuleNo("003");
		entity.generateId();
		entity.setRuleName("遗漏均衡");
		entity.setStatus(StatusType.INACTIVE.getCode());
		entity.setRuleDesc("根据权重map统计遗漏次数");
		map = new HashMap<Integer, Integer>();
		map.put(0, 25);
		map.put(1000, 10);
		map.put(500, 5);
		map.put(200, 2);
		map.put(100, 1);
		entity.setPeriodsAndweights(map);
		entity.setExcuteStatus(ExcuteStatusType.STOP.name());
		entity.setCreateTime(new Date());
		entity.setUpdateTime(new Date());
		list.add(entity);

		dao.insert(list);
	}
}

/**
 * 
 */
package win.caicaikan.service.internal;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import win.caicaikan.BaseTest;
import win.caicaikan.constant.ExecuteStatus;
import win.caicaikan.constant.LotteryType;
import win.caicaikan.constant.RuleType;
import win.caicaikan.repository.mongodb.entity.PredictRuleEntity;
import win.caicaikan.repository.mongodb.entity.SysConfigEntity;
import win.caicaikan.repository.mongodb.entity.TaskEntity;
import win.caicaikan.util.GsonUtil;

/**
 * @Desc
 * @author wewenge.yan
 * @Date 2016年12月28日
 * @ClassName DaoServiceTest
 */
public class DaoServiceTest extends BaseTest {
	@Autowired
	private DaoService daoService;

	@Test
	public void testInsertSysConfig() {
		SysConfigEntity entity = new SysConfigEntity();
		entity.setPrimaryKey("SP", "00", "00");
		entity.setName("双色球下期期号");
		entity.setKey("2016-12-27 21:20:40");
		entity.setValue("2016152");
		entity.setStatus(1);
		entity.setCreateTime(new Date());
		entity.setUpdateTime(new Date());
		daoService.insert(entity);
	}

	@Test
	public void testInsertTasks() {
		TaskEntity entity = new TaskEntity();
		entity.setId("SsqCurrentTask");
		entity.setExecuteStatus(ExecuteStatus.FAILED.name());
		daoService.insert(entity);

		entity = new TaskEntity();
		entity.setId("SsqHistoryTask");
		entity.setExecuteStatus(ExecuteStatus.FAILED.name());
		daoService.insert(entity);

		entity = new TaskEntity();
		entity.setId("SsqPredictTask");
		entity.setExecuteStatus(ExecuteStatus.FAILED.name());
		daoService.insert(entity);
	}

	@Test
	public void testInsertRules() {
		PredictRuleEntity rule = null;
		int[] termCounts = { 2000, 1000, 500, 200, 100, 50 };
		for (int i = 0; i < termCounts.length; i++) {
			for (RuleType ruleType : RuleType.values()) {
				if (ruleType.equals(RuleType.MULTI)) {
					continue;
				}
				rule = new PredictRuleEntity();
				rule.setPrimaryKey(LotteryType.SSQ.getCode(), ruleType.name(), termCounts[i]);
				rule.setRuleName(ruleType.getDesc() + termCounts[i]);
				rule.setStatus(1);
				rule.setExecuteStatus(ExecuteStatus.SUCCESS.name());
				rule.setCreateTime(new Date());
				rule.setUpdateTime(new Date());
				daoService.save(rule);
			}
		}

		for (int i = 0; i < termCounts.length; i++) {
			for (int j = 10; j < 90; j += 10) {
				for (int k = 0; k <= 10 && j + k < 100; k += 10) {
					String id = j + "," + (100 - j - k) + "," + k;
					rule = new PredictRuleEntity();
					rule.setPrimaryKey(LotteryType.SSQ.getCode(), RuleType.MULTI.name(), termCounts[i]);
					rule.setId(rule.getId() + "-" + id);
					rule.setRuleName(RuleType.MULTI.getDesc() + termCounts[i] + ",比例" + id);
					rule.setStatus(1);
					rule.setExecuteStatus(ExecuteStatus.SUCCESS.name());
					rule.setCreateTime(new Date());
					rule.setUpdateTime(new Date());
					Map<String, Integer> map = new HashMap<>();
					map.put(RuleType.DISPLAY_TIMES.name(), j);
					map.put(RuleType.SKIP_TIMES.name(), 100 - j - k);
					map.put(RuleType.DOUBLE_TIMES.name(), k);
					String ruleAndweights = GsonUtil.toJson(map);
					rule.setRuleAndweights(ruleAndweights);
					daoService.save(rule);
				}
			}
		}
	}
}

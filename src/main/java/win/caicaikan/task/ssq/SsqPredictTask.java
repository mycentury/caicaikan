/**
 * 
 */
package win.caicaikan.task.ssq;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import win.caicaikan.constant.ExecuteStatus;
import win.caicaikan.constant.RuleType;
import win.caicaikan.repository.mongodb.dao.PredictRuleDao;
import win.caicaikan.repository.mongodb.dao.ssq.SsqPredictDao;
import win.caicaikan.repository.mongodb.dao.ssq.SsqResultDao;
import win.caicaikan.repository.mongodb.entity.PredictRuleEntity;
import win.caicaikan.repository.mongodb.entity.ssq.SsqPredictEntity;
import win.caicaikan.repository.mongodb.entity.ssq.SsqResultEntity;
import win.caicaikan.service.rule.RuleTemplate;
import win.caicaikan.task.TaskTemplete;
import win.caicaikan.util.GsonUtil;
import win.caicaikan.util.MapUtil;
import win.caicaikan.util.SpringContextUtil;

import com.google.gson.reflect.TypeToken;

/**
 * @Desc 双色球及时任务：用于下期预测
 * @author wewenge.yan
 * @Date 2016年11月21日
 * @ClassName PredictTask
 */
@Service
public class SsqPredictTask extends TaskTemplete {
	private static final Logger logger = Logger.getLogger(SsqPredictTask.class);
	@Autowired
	private SsqResultDao ssqResultDao;
	@Autowired
	private SsqPredictDao ssqPredictDao;
	@Autowired
	private PredictRuleDao predictRuleDao;

	@Override
	@Scheduled(cron = "${task.cron.ssq.predict}")
	public void execute() {
		super.execute();
	}

	@Override
	public void doInTask() {
		Sort sort = new Sort(new Sort.Order(Sort.Direction.DESC, "termNo"));
		List<SsqResultEntity> list = ssqResultDao.findAll(sort);
		this.predictByResults(list);
	}

	public void predictByResults(List<SsqResultEntity> list) {
		Map<String, RuleTemplate> beans = SpringContextUtil.getBeans(RuleTemplate.class);
		List<PredictRuleEntity> rules = predictRuleDao.findAll();
		for (PredictRuleEntity rule : rules) {
			rule.setExecuteStatus(ExecuteStatus.REDAY.name());
		}
		predictRuleDao.save(rules);

		List<SsqPredictEntity> predicts = excuteBaseRules(beans, list, rules);
		ssqPredictDao.save(predicts);
		predicts = excuteGeneRules(predicts, rules);
		ssqPredictDao.save(predicts);
	}

	private List<SsqPredictEntity> excuteGeneRules(List<SsqPredictEntity> basePredicts, List<PredictRuleEntity> rules) {
		List<SsqPredictEntity> result = new ArrayList<SsqPredictEntity>();
		Map<String, SsqPredictEntity> map = new HashMap<String, SsqPredictEntity>();
		for (SsqPredictEntity basePredict : basePredicts) {
			map.put(basePredict.getRuleId(), basePredict);
		}

		for (PredictRuleEntity rule : rules) {
			if (!RuleType.MULTI.name().equals(rule.getRuleType())) {
				continue;
			}
			rule.setExecuteStatus(ExecuteStatus.RUNNING.name());
			predictRuleDao.save(rule);
			SsqPredictEntity entity = excuteGeneRule(rule, map, basePredicts);
			rule.setExecuteStatus(ExecuteStatus.SUCCESS.name());
			predictRuleDao.save(rule);
			if (entity != null) {
				result.add(entity);
			}
		}
		return result;
	}

	private List<String> addTwoList(List<String> numberList1, List<String> numberList2) {
		if (numberList1 == null) {
			return numberList2;
		}
		if (numberList2 == null) {
			return numberList1;
		}
		List<String> result = new ArrayList<String>();
		for (String redNumber1 : numberList1) {
			String[] split1 = redNumber1.split("=");
			for (String redNumber2 : numberList2) {
				String[] split2 = redNumber2.split("=");
				if (split1[0].equals(split2[0])) {
					int sum = Integer.valueOf(split1[1]) + Integer.valueOf(split2[1]);
					redNumber1 = split1[0] + "=" + sum;
				}
			}
		}
		return result;
	}

	private List<SsqPredictEntity> excuteBaseRules(Map<String, RuleTemplate> beans, List<SsqResultEntity> list, List<PredictRuleEntity> rules) {
		List<SsqPredictEntity> result = new ArrayList<SsqPredictEntity>();
		for (PredictRuleEntity rule : rules) {
			if (RuleType.MULTI.name().equals(rule.getRuleType())) {
				continue;
			}
			for (RuleTemplate ruleExcutor : beans.values()) {
				if (ruleExcutor.getRuleType().name().equals(rule.getRuleType())) {
					try {
						rule.setExecuteStatus(ExecuteStatus.RUNNING.name());
						predictRuleDao.save(rule);
						SsqPredictEntity predict = ruleExcutor.excute(list, rule);
						rule.setExecuteStatus(ExecuteStatus.SUCCESS.name());
						predictRuleDao.save(rule);
						result.add(predict);
					} catch (Exception e) {
						logger.error("rule.excuteRules() excute error");
						e.printStackTrace();
					}
				}
			}
		}
		return result;
	}

	private SsqPredictEntity excuteGeneRule(PredictRuleEntity rule, Map<String, SsqPredictEntity> map, List<SsqPredictEntity> basePredicts) {
		List<String> redNumbers = null;
		List<String> blueNumbers = null;
		Type typeOfT = new TypeToken<Map<String, Integer>>() {
		}.getType();
		Map<String, Integer> ruleAndweights = GsonUtil.fromJson(rule.getRuleAndweights(), typeOfT);
		for (Entry<String, Integer> entry : ruleAndweights.entrySet()) {
			SsqPredictEntity ssqPredictEntity = map.get(entry.getKey());
			if (ssqPredictEntity == null) {
				logger.error("未找到该规则的预测结果：ruleId=" + entry.getKey());
				return null;
			}
			redNumbers = addTwoList(redNumbers, ssqPredictEntity.getRedNumbers());
			blueNumbers = addTwoList(blueNumbers, ssqPredictEntity.getBlueNumbers());
		}

		Map<String, Integer> redMap = new HashMap<String, Integer>();
		for (String redNumber : redNumbers) {
			String[] split = redNumber.split("=");
			redMap.put(split[0], Integer.parseInt(split[1]));
		}
		redNumbers = MapUtil.sortMapToList(redMap, "=", MapUtil.DESC);

		Map<String, Integer> blueMap = new HashMap<String, Integer>();
		for (String blueNumber : blueNumbers) {
			String[] split = blueNumber.split("=");
			blueMap.put(split[0], Integer.parseInt(split[1]));
		}
		blueNumbers = MapUtil.sortMapToList(blueMap, "=", MapUtil.DESC);

		SsqPredictEntity entity = new SsqPredictEntity();
		String ruleId = rule.getId();
		String termNo = basePredicts.get(0).getTermNo();
		entity.setPrimaryKey(ruleId, termNo);
		entity.setRedNumbers(redNumbers);
		entity.setBlueNumbers(blueNumbers);
		entity.setCreateTime(new Date());
		entity.setUpdateTime(new Date());
		return entity;
	}
}

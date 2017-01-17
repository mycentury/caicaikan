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
import org.springframework.data.domain.Sort.Direction;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import win.caicaikan.constant.ExecuteStatus;
import win.caicaikan.constant.RuleType;
import win.caicaikan.repository.mongodb.entity.PredictRuleEntity;
import win.caicaikan.repository.mongodb.entity.ssq.SsqPredictEntity;
import win.caicaikan.repository.mongodb.entity.ssq.SsqResultEntity;
import win.caicaikan.service.internal.DaoService;
import win.caicaikan.service.internal.DaoService.Condition;
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
	private DaoService daoService;

	@Override
	@Scheduled(cron = "${task.cron.ssq.predict}")
	public void execute() {
		super.execute();
	}

	@Override
	public void doInTask() {
		Condition condition = new Condition();
		List<PredictRuleEntity> rules = daoService.query(condition, PredictRuleEntity.class);
		condition.setOrder(Direction.DESC);
		condition.setOrderBy(daoService.getIdColName(SsqResultEntity.class));
		List<SsqResultEntity> results = daoService.query(condition, SsqResultEntity.class);
		this.predictByResults(results, rules);
	}

	public void predictByResults(List<SsqResultEntity> list, List<PredictRuleEntity> rules) {
		Map<String, RuleTemplate> beans = SpringContextUtil.getBeans(RuleTemplate.class);
		for (PredictRuleEntity rule : rules) {
			rule.setExecuteStatus(ExecuteStatus.REDAY.name());
			daoService.save(rule);
		}

		List<SsqPredictEntity> predicts = this.excuteBaseRules(beans, list, rules);
		for (SsqPredictEntity ssqPredictEntity : predicts) {
			daoService.save(ssqPredictEntity);
		}
		predicts = this.excuteGeneRules(predicts, rules, list.size());
		for (SsqPredictEntity ssqPredictEntity : predicts) {
			daoService.save(ssqPredictEntity);
		}
	}

	private List<SsqPredictEntity> excuteGeneRules(List<SsqPredictEntity> basePredicts, List<PredictRuleEntity> rules, int terms) {
		List<SsqPredictEntity> result = new ArrayList<SsqPredictEntity>();
		Map<String, SsqPredictEntity> map = new HashMap<String, SsqPredictEntity>();
		for (SsqPredictEntity basePredict : basePredicts) {
			map.put(basePredict.getRuleId(), basePredict);
		}

		for (PredictRuleEntity rule : rules) {
			if (!RuleType.MULTI.name().equals(rule.getRuleType()) || rule.getTerms() > terms) {
				rule.setExecuteStatus(ExecuteStatus.SUCCESS.name());
				daoService.save(rule);
				continue;
			}
			rule.setExecuteStatus(ExecuteStatus.RUNNING.name());
			daoService.save(rule);
			SsqPredictEntity entity = this.excuteGeneRule(rule, map, basePredicts);
			rule.setExecuteStatus(ExecuteStatus.SUCCESS.name());
			daoService.save(rule);
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
		for (int i = 0; i < numberList1.size(); i++) {
			String redNumber1 = numberList1.get(i);
			String[] split1 = redNumber1.split("=");
			for (String redNumber2 : numberList2) {
				String[] split2 = redNumber2.split("=");
				if (split1[0].equals(split2[0])) {
					int sum = Integer.valueOf(split1[1]) + Integer.valueOf(split2[1]);
					redNumber1 = split1[0] + "=" + sum;
					numberList1.set(i, redNumber1);
					break;
				}
			}
		}
		return numberList1;
	}

	private List<SsqPredictEntity> excuteBaseRules(Map<String, RuleTemplate> beans, List<SsqResultEntity> list, List<PredictRuleEntity> rules) {
		List<SsqPredictEntity> result = new ArrayList<SsqPredictEntity>();
		for (PredictRuleEntity rule : rules) {
			if (RuleType.MULTI.name().equals(rule.getRuleType()) || rule.getTerms() > list.size()) {
				rule.setExecuteStatus(ExecuteStatus.SKIPED.name());
				daoService.save(rule);
				continue;
			}
			RuleTemplate ruleExcutor = null;
			for (RuleTemplate excutor : beans.values()) {
				if (excutor.getRuleType().name().equals(rule.getRuleType())) {
					ruleExcutor = excutor;
					break;
				}
			}
			if (ruleExcutor == null) {
				rule.setExecuteStatus(ExecuteStatus.SKIPED.name());
				daoService.save(rule);
				continue;
			}
			try {
				rule.setExecuteStatus(ExecuteStatus.RUNNING.name());
				daoService.save(rule);
				SsqPredictEntity predict = ruleExcutor.excute(list, rule);
				result.add(predict);
				rule.setExecuteStatus(ExecuteStatus.SUCCESS.name());
				daoService.save(rule);
			} catch (Exception e) {
				logger.error("rule.excuteRules() excute error");
				e.printStackTrace();
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
			if (entry.getValue() == null || entry.getValue() == 0) {
				continue;
			}
			String ruleId = rule.getLotteryType() + "-" + entry.getKey() + "-" + rule.getTerms();
			SsqPredictEntity ssqPredictEntity = map.get(ruleId);
			if (ssqPredictEntity == null) {
				logger.info("未找到该基础规则的预测结果，请手动创建该规则:" + ruleId);
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

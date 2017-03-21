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
import org.springframework.util.CollectionUtils;

import win.caicaikan.constant.ExecuteStatus;
import win.caicaikan.constant.RuleType;
import win.caicaikan.repository.mongodb.entity.PredictRuleEntity;
import win.caicaikan.repository.mongodb.entity.ssq.SsqPredictEntity;
import win.caicaikan.repository.mongodb.entity.ssq.SsqRecommendEntity;
import win.caicaikan.repository.mongodb.entity.ssq.SsqResultEntity;
import win.caicaikan.service.internal.DaoService;
import win.caicaikan.service.internal.DaoService.Condition;
import win.caicaikan.service.rule.RuleService;
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
	@Autowired
	private RuleService ruleService;

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
		// 根据预测规则的几率和预测结果计算最佳组合
		String nextTermNo = ruleService.getNextTermNoOfSsq(results.get(0));
		this.recommendBest(rules, nextTermNo, 12, 5);
	}

	/**
	 * @param rules
	 */
	public void recommendBest(List<PredictRuleEntity> rules, String nextTermNo, int redCount, int blueCount) {
		PredictRuleEntity[][] maxRateRules = new PredictRuleEntity[7][2];
		boolean hasInit = false;
		for (int i = 0; i < rules.size(); i++) {
			PredictRuleEntity rule = rules.get(i);
			if (RuleType.DOUBLE_TIMES.name().equals(rule.getRuleType())) {
				continue;
			}
			if (!hasInit) {
				for (int j = 0; j < maxRateRules.length; j++) {
					maxRateRules[j][0] = rules.get(0);
					maxRateRules[j][1] = rules.get(0);
				}
				hasInit = true;
			}
			int number;
			// 1
			number = 0;
			if (rule.getFirstRedRate() > maxRateRules[number][0].getFirstRedRate()) {
				// 第一与第二相同，或者与第二相同，或者与第一第二都不同
				if (maxRateRules[number][0].getRuleType().equals(maxRateRules[number][1]) || rule.getRuleType().equals(maxRateRules[number][0])
						|| (!rule.getRuleType().equals(maxRateRules[number][0]) && !rule.getRuleType().equals(maxRateRules[number][1]))) {
					maxRateRules[number][1] = maxRateRules[number][0];
				}
				maxRateRules[number][0] = rule;
			} else if (rule.getFirstRedRate() > maxRateRules[number][1].getFirstRedRate()) {
				if (maxRateRules[number][0].getRuleType().equals(maxRateRules[number][1]) || !rule.getRuleType().equals(maxRateRules[number][0])) {
					maxRateRules[number][1] = rule;
				}
			}
			// 2
			number = 1;
			if (rule.getSecondRedRate() > maxRateRules[number][0].getSecondRedRate()) {
				// 第一与第二相同，或者与第二相同，或者与第一第二都不同
				if (maxRateRules[number][0].getRuleType().equals(maxRateRules[number][1]) || rule.getRuleType().equals(maxRateRules[number][0])
						|| (!rule.getRuleType().equals(maxRateRules[number][0]) && !rule.getRuleType().equals(maxRateRules[number][1]))) {
					maxRateRules[number][1] = maxRateRules[number][0];
				}
				maxRateRules[number][0] = rule;
			} else if (rule.getSecondRedRate() > maxRateRules[number][1].getSecondRedRate()) {
				if (maxRateRules[number][0].getRuleType().equals(maxRateRules[number][1]) || !rule.getRuleType().equals(maxRateRules[number][0])) {
					maxRateRules[number][1] = rule;
				}
			}
			// 3
			number = 2;
			if (rule.getThirdRedRate() > maxRateRules[number][0].getThirdRedRate()) {
				// 第一与第二相同，或者与第二相同，或者与第一第二都不同
				if (maxRateRules[number][0].getRuleType().equals(maxRateRules[number][1]) || rule.getRuleType().equals(maxRateRules[number][0])
						|| (!rule.getRuleType().equals(maxRateRules[number][0]) && !rule.getRuleType().equals(maxRateRules[number][1]))) {
					maxRateRules[number][1] = maxRateRules[number][0];
				}
				maxRateRules[number][0] = rule;
			} else if (rule.getThirdRedRate() > maxRateRules[number][1].getThirdRedRate()) {
				if (maxRateRules[number][0].getRuleType().equals(maxRateRules[number][1]) || !rule.getRuleType().equals(maxRateRules[number][0])) {
					maxRateRules[number][1] = rule;
				}
			}
			// 4
			number = 3;
			if (rule.getForthRedRate() > maxRateRules[number][0].getForthRedRate()) {
				// 第一与第二相同，或者与第二相同，或者与第一第二都不同
				if (maxRateRules[number][0].getRuleType().equals(maxRateRules[number][1]) || rule.getRuleType().equals(maxRateRules[number][0])
						|| (!rule.getRuleType().equals(maxRateRules[number][0]) && !rule.getRuleType().equals(maxRateRules[number][1]))) {
					maxRateRules[number][1] = maxRateRules[number][0];
				}
				maxRateRules[number][0] = rule;
			} else if (rule.getForthRedRate() > maxRateRules[number][1].getForthRedRate()) {
				if (maxRateRules[number][0].getRuleType().equals(maxRateRules[number][1]) || !rule.getRuleType().equals(maxRateRules[number][0])) {
					maxRateRules[number][1] = rule;
				}
			}
			// 5
			number = 4;
			if (rule.getFifthRedRate() > maxRateRules[number][0].getFifthRedRate()) {
				// 第一与第二相同，或者与第二相同，或者与第一第二都不同
				if (maxRateRules[number][0].getRuleType().equals(maxRateRules[number][1]) || rule.getRuleType().equals(maxRateRules[number][0])
						|| (!rule.getRuleType().equals(maxRateRules[number][0]) && !rule.getRuleType().equals(maxRateRules[number][1]))) {
					maxRateRules[number][1] = maxRateRules[number][0];
				}
				maxRateRules[number][0] = rule;
			} else if (rule.getFifthRedRate() > maxRateRules[number][1].getFifthRedRate()) {
				if (maxRateRules[number][0].getRuleType().equals(maxRateRules[number][1]) || !rule.getRuleType().equals(maxRateRules[number][0])) {
					maxRateRules[number][1] = rule;
				}
			}
			// 6
			number = 5;
			if (rule.getSixthRedRate() > maxRateRules[number][0].getSixthRedRate()) {
				// 第一与第二相同，或者与第二相同，或者与第一第二都不同
				if (maxRateRules[number][0].getRuleType().equals(maxRateRules[number][1]) || rule.getRuleType().equals(maxRateRules[number][0])
						|| (!rule.getRuleType().equals(maxRateRules[number][0]) && !rule.getRuleType().equals(maxRateRules[number][1]))) {
					maxRateRules[number][1] = maxRateRules[number][0];
				}
				maxRateRules[number][0] = rule;
			} else if (rule.getSixthRedRate() > maxRateRules[number][1].getSixthRedRate()) {
				if (maxRateRules[number][0].getRuleType().equals(maxRateRules[number][1]) || !rule.getRuleType().equals(maxRateRules[number][0])) {
					maxRateRules[number][1] = rule;
				}
			}
			// 7
			number = 6;
			if (rule.getBlueRate() > maxRateRules[number][0].getBlueRate()) {
				// 第一与第二相同，或者与第二相同，或者与第一第二都不同
				if (maxRateRules[number][0].getRuleType().equals(maxRateRules[number][1]) || rule.getRuleType().equals(maxRateRules[number][0])
						|| (!rule.getRuleType().equals(maxRateRules[number][0]) && !rule.getRuleType().equals(maxRateRules[number][1]))) {
					maxRateRules[number][1] = maxRateRules[number][0];
				}
				maxRateRules[number][0] = rule;
			} else if (rule.getBlueRate() > maxRateRules[number][1].getBlueRate()) {
				if (maxRateRules[number][0].getRuleType().equals(maxRateRules[number][1]) || !rule.getRuleType().equals(maxRateRules[number][0])) {
					maxRateRules[number][1] = rule;
				}
			}
		}
		int min = 1;
		int max = 33 / 6 * 2;
		StringBuilder sb = new StringBuilder();

		Map<String, Integer> allRedMap = new HashMap<String, Integer>();
		for (int i = 0; i < 6; i++) {
			Map<String, Integer> redmap = new HashMap<String, Integer>();
			for (int j = 0; j < 2; j++) {
				String predictId = maxRateRules[i][j].getId() + "-" + nextTermNo;
				SsqPredictEntity queryById = daoService.queryById(predictId, SsqPredictEntity.class);
				for (int k = 0; k < redCount; k++) {
					String red = queryById.getRedNumbers().get(k).split("=")[0];
					int number = Integer.valueOf(red);
					if (number >= min && number <= max) {
						int original = redmap.get(red) == null ? 0 : redmap.get(red);
						redmap.put(red, original + 2 - j + redCount - k);
						original = allRedMap.get(red) == null ? 0 : allRedMap.get(red);
						allRedMap.put(red, original + 2 - j + redCount - k);
					}
				}
			}
			if (CollectionUtils.isEmpty(redmap)) {
				recommendBest(rules, nextTermNo, redCount + 1, blueCount);
				return;
			}
			List<String> sortMapToList = MapUtil.sortMapToList(redmap, "=", MapUtil.DESC);
			String red = sortMapToList.get(0).split("=")[0];
			min = Math.max(Integer.valueOf(red), i) + 1;
			max = Math.min(33 / 6 * (i + 3), 28 + i);
			sb.append(i == 0 ? "" : ",").append(red);
		}

		Map<String, Integer> blueMap = new HashMap<String, Integer>();
		for (int j = 0; j < 2; j++) {
			String predictId = maxRateRules[6][j].getId() + "-" + nextTermNo;
			SsqPredictEntity queryById = daoService.queryById(predictId, SsqPredictEntity.class);
			for (int k = 0; k < blueCount; k++) {
				String blue = queryById.getBlueNumbers().get(k).split("=")[0];
				int original = blueMap.get(blue) == null ? 0 : blueMap.get(blue);
				blueMap.put(blue, original + 2 - j + blueCount - k);
			}
		}

		if (CollectionUtils.isEmpty(blueMap)) {
			recommendBest(rules, nextTermNo, redCount, blueCount + 1);
			return;
		}
		sb.append("/");
		List<String> redList = MapUtil.sortMapToList(allRedMap, "=", MapUtil.DESC);
		min = 1;
		max = 33 / 6 * 2;
		for (int i = 0; i < 6 && i < redList.size(); i++) {
			String red = redList.get(i).split("=")[0];
			min = Math.max(Integer.valueOf(red), i) + 1;
			max = Math.min(33 / 6 * (i + 3), 33 - i + 1);
			sb.append(i == 0 ? "" : ",").append(red);
		}

		List<String> blueList = MapUtil.sortMapToList(blueMap, "=", MapUtil.DESC);
		sb.append("+");
		for (int i = 0; i < blueCount && i < blueList.size(); i++) {
			String blue = blueList.get(i).split("=")[0];
			sb.append(i == 0 ? "" : ",").append(blue);
		}
		SsqRecommendEntity entity = new SsqRecommendEntity();
		entity.setPrimaryKey("20170316", nextTermNo);
		entity.setRecommendNumbers(sb.toString());
		entity.setRedNumbers(redList);
		entity.setBlueNumbers(blueList);

		daoService.save(entity);
	}

	public void predictByResults(List<SsqResultEntity> list, List<PredictRuleEntity> rules) {
		Map<String, RuleTemplate> beans = SpringContextUtil.getBeans(RuleTemplate.class);
		for (PredictRuleEntity rule : rules) {
			rule.setExecuteStatus(ExecuteStatus.REDAY.name());
			daoService.save(rule);
		}

		List<SsqPredictEntity> predicts = this.excuteBaseRules(beans, list, rules);
		daoService.save(predicts);
		predicts = this.excuteGeneRules(predicts, rules, list.size());
		daoService.save(predicts);
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

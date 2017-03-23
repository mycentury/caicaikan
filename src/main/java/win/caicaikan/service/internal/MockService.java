package win.caicaikan.service.internal;

import java.lang.reflect.Type;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import win.caicaikan.constant.ExecuteStatus;
import win.caicaikan.constant.LotteryType;
import win.caicaikan.constant.RuleType;
import win.caicaikan.constant.SsqConstant;
import win.caicaikan.repository.mongodb.entity.MockRuleEntity;
import win.caicaikan.repository.mongodb.entity.ssq.MockPredictEntity;
import win.caicaikan.repository.mongodb.entity.ssq.MockRecommendEntity;
import win.caicaikan.repository.mongodb.entity.ssq.MockResultEntity;
import win.caicaikan.repository.mongodb.entity.ssq.SsqPredictEntity;
import win.caicaikan.service.internal.DaoService.Condition;
import win.caicaikan.service.rule.RuleTemplate.Result;
import win.caicaikan.util.GsonUtil;
import win.caicaikan.util.MapUtil;
import win.caicaikan.util.MathUtil;

import com.google.gson.reflect.TypeToken;

@Service
public class MockService {
	private static final int SIZE = 1000;
	private static final int START_NO = 201700001;
	private static final int END_NO = 201799999;
	private static final Logger logger = Logger.getLogger(MockService.class);
	@Autowired
	private DaoService daoService;

	public void initMockResults() {
		daoService.delete(null, MockResultEntity.class);
		List<MockResultEntity> list = new ArrayList<MockResultEntity>();
		for (int termNo = START_NO; termNo <= END_NO; termNo++) {
			String[] redNumbers = Arrays.copyOf(SsqConstant.RED_NUMBERS,
					SsqConstant.RED_NUMBERS.length);
			String[] blueNumbers = Arrays.copyOf(SsqConstant.BLUE_NUMBERS,
					SsqConstant.BLUE_NUMBERS.length);
			Random random = new Random();
			List<String> redList = new ArrayList<String>();
			while (redList.size() < 6) {
				String e = redNumbers[random.nextInt(redNumbers.length)];
				if (!redList.contains(e)) {
					redList.add(e);
				}
			}
			Collections.sort(redList);
			String blueNumber = blueNumbers[random.nextInt(blueNumbers.length)];
			MockResultEntity mockResult = new MockResultEntity();
			mockResult.setId(String.valueOf(termNo));
			mockResult.setReds(redList.toString().replaceAll("[\\[\\]\\s]", ""));
			mockResult.setBlue(blueNumber);

			list.add(mockResult);
			if (list.size() >= SIZE || termNo >= END_NO) {
				daoService.insert(list, MockResultEntity.class);
				list.clear();
			}
		}
	}

	public void initMockPredicts() {
		Condition condition = new Condition();
		List<MockRuleEntity> rules = daoService.query(condition, MockRuleEntity.class);

		int startNo = START_NO;
		int endNo = startNo + SIZE;
		String idColName = daoService.getIdColName(MockRuleEntity.class);
		condition.setOrder(Direction.DESC);
		condition.setOrderBy(idColName);
		condition.addParam(idColName, "<=", String.valueOf(endNo - 1));
		condition.setLimit(SIZE);
		List<MockResultEntity> results = daoService.query(condition, MockResultEntity.class);
		logger.info("result from " + results.get(0) + " to " + results.get(results.size() - 1));
		for (; endNo < END_NO; endNo++) {
			// 预测
			logger.info("predict for term " + endNo);
			this.predictByResults(results, rules);
			// 填充正确位置
			logger.info("calculate right positions of term " + endNo);
			MockResultEntity nextTerm = daoService.queryById(String.valueOf(endNo),
					MockResultEntity.class);
			condition = new Condition();
			condition.addParam("termNo", "=", String.valueOf(endNo));
			List<MockPredictEntity> list = daoService.query(condition, MockPredictEntity.class);
			for (MockPredictEntity mockPredict : list) {
				String rightPositions = this.getRightNumPositions(mockPredict, nextTerm);
				mockPredict.setRightNumbers(rightPositions);
				daoService.save(mockPredict);
			}
			results.add(0, nextTerm);
			results.remove(results.size() - 1);
		}
		logger.info("calculate rate");
		// 计算概率
		for (MockRuleEntity rule : rules) {
			rule = this.countRate(rule, 6, 1);
			daoService.save(rule);
		}
		logger.info("calculate rate end");
		endNo = startNo + SIZE;
		for (; endNo < END_NO; endNo++) {
			// 根据预测规则的几率和预测结果计算最佳组合
			String nextTermNo = String.valueOf(endNo);
			logger.info("recommend best of nextTermNo");
			this.recommendBest(rules, nextTermNo, 10, 5);
		}
	}

	public void initPredictRightPositions() {
		Condition condition = new Condition();
		List<MockResultEntity> mockResults = daoService.query(condition, MockResultEntity.class);
		List<MockRuleEntity> mockRules = daoService.query(condition, MockRuleEntity.class);
		for (MockRuleEntity mockRule : mockRules) {
			condition = new Condition();
			condition.addParam("ruleId", "=", mockRule.getId());
			List<MockPredictEntity> mockPredicts = daoService.query(condition,
					MockPredictEntity.class);
			Double redPositionCount = 0D;
			Double bluePositionCount = 0D;
			for (MockPredictEntity mockPredict : mockPredicts) {
				for (MockResultEntity mockResult : mockResults) {
					if (mockPredict.getTermNo().equals(mockResult.getId())) {
						String rightPositions = this.getRightNumPositions(mockPredict, mockResult);
						String[] numPositions = rightPositions.split("\\+");
						String[] redPositions = numPositions[0].split(",");
						for (String red : redPositions) {
							redPositionCount += Integer.valueOf(red);
						}
						Integer bluePosition = Integer.valueOf(numPositions[1].split(",")[0]);
						bluePositionCount += bluePosition;
						if (StringUtils.isEmpty(rightPositions)) {
							logger.error("rightPositions null");
						}
						System.out.println();
						mockPredict.setRightNumbers(rightPositions);
						daoService.save(mockPredict);
						break;
					}
				}
			}

			redPositionCount = (mockPredicts.size() * 6 * 17) / redPositionCount;
			bluePositionCount = (mockPredicts.size() * 8.5) / bluePositionCount;
			mockRule.setMultipleRedRate(redPositionCount);
			mockRule.setMultipleBlueRate(bluePositionCount);
			daoService.save(mockRule);
		}

	}

	public void initMockRules() {
		List<MockRuleEntity> rules = new ArrayList<MockRuleEntity>();
		MockRuleEntity rule = null;
		int[] termCounts = { 1000, 500, 200, 100 };
		for (int i = 0; i < termCounts.length; i++) {
			for (RuleType ruleType : RuleType.values()) {
				if (ruleType.equals(RuleType.MULTI)) {
					continue;
				}
				rule = new MockRuleEntity();
				rule.setPrimaryKey(LotteryType.SSQ.getCode(), ruleType.name(), termCounts[i]);
				rule.setRuleName(ruleType.getDesc() + termCounts[i]);
				rule.setStatus(1);
				rule.setExecuteStatus(ExecuteStatus.SUCCESS.name());
				rule.setCreateTime(new Date());
				rule.setUpdateTime(new Date());
				rules.add(rule);
			}
		}

		for (int i = 0; i < termCounts.length; i++) {
			for (int j = 30; j < 100; j += 30) {
				for (int k = 0; k < 2; k++) {
					String id = j + "," + (100 - j) + "," + 10 * k;
					rule = new MockRuleEntity();
					rule.setPrimaryKey(LotteryType.SSQ.getCode(), RuleType.MULTI.name(),
							termCounts[i]);
					rule.setId(rule.getId() + "-" + id);
					rule.setRuleName(RuleType.MULTI.getDesc() + termCounts[i] + ",比例" + id);
					rule.setStatus(1);
					rule.setExecuteStatus(ExecuteStatus.SUCCESS.name());
					rule.setCreateTime(new Date());
					rule.setUpdateTime(new Date());
					Map<String, Integer> map = new HashMap<>();
					map.put(RuleType.DISPLAY_TIMES.name(), j);
					map.put(RuleType.SKIP_TIMES.name(), 100 - j);
					map.put(RuleType.DOUBLE_TIMES.name(), 10 * k);
					String ruleAndweights = GsonUtil.toJson(map);
					rule.setRuleAndweights(ruleAndweights);
					rules.add(rule);
				}
			}
		}
		daoService.delete(null, MockRuleEntity.class);
		daoService.insert(rules, MockRuleEntity.class);
	}

	/**
	 * @param rules
	 */
	private void recommendBest(List<MockRuleEntity> rules, String nextTermNo, int redCount,
			int blueCount) {
		MockRuleEntity[][] maxRateRules = new MockRuleEntity[7][2];
		boolean hasInit = false;
		for (int i = 0; i < rules.size(); i++) {
			MockRuleEntity rule = rules.get(i);
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
				if (maxRateRules[number][0].getRuleType().equals(maxRateRules[number][1])
						|| rule.getRuleType().equals(maxRateRules[number][0])
						|| (!rule.getRuleType().equals(maxRateRules[number][0]) && !rule
								.getRuleType().equals(maxRateRules[number][1]))) {
					maxRateRules[number][1] = maxRateRules[number][0];
				}
				maxRateRules[number][0] = rule;
			} else if (rule.getFirstRedRate() > maxRateRules[number][1].getFirstRedRate()) {
				if (maxRateRules[number][0].getRuleType().equals(maxRateRules[number][1])
						|| !rule.getRuleType().equals(maxRateRules[number][0])) {
					maxRateRules[number][1] = rule;
				}
			}
			// 2
			number = 1;
			if (rule.getSecondRedRate() > maxRateRules[number][0].getSecondRedRate()) {
				// 第一与第二相同，或者与第二相同，或者与第一第二都不同
				if (maxRateRules[number][0].getRuleType().equals(maxRateRules[number][1])
						|| rule.getRuleType().equals(maxRateRules[number][0])
						|| (!rule.getRuleType().equals(maxRateRules[number][0]) && !rule
								.getRuleType().equals(maxRateRules[number][1]))) {
					maxRateRules[number][1] = maxRateRules[number][0];
				}
				maxRateRules[number][0] = rule;
			} else if (rule.getSecondRedRate() > maxRateRules[number][1].getSecondRedRate()) {
				if (maxRateRules[number][0].getRuleType().equals(maxRateRules[number][1])
						|| !rule.getRuleType().equals(maxRateRules[number][0])) {
					maxRateRules[number][1] = rule;
				}
			}
			// 3
			number = 2;
			if (rule.getThirdRedRate() > maxRateRules[number][0].getThirdRedRate()) {
				// 第一与第二相同，或者与第二相同，或者与第一第二都不同
				if (maxRateRules[number][0].getRuleType().equals(maxRateRules[number][1])
						|| rule.getRuleType().equals(maxRateRules[number][0])
						|| (!rule.getRuleType().equals(maxRateRules[number][0]) && !rule
								.getRuleType().equals(maxRateRules[number][1]))) {
					maxRateRules[number][1] = maxRateRules[number][0];
				}
				maxRateRules[number][0] = rule;
			} else if (rule.getThirdRedRate() > maxRateRules[number][1].getThirdRedRate()) {
				if (maxRateRules[number][0].getRuleType().equals(maxRateRules[number][1])
						|| !rule.getRuleType().equals(maxRateRules[number][0])) {
					maxRateRules[number][1] = rule;
				}
			}
			// 4
			number = 3;
			if (rule.getForthRedRate() > maxRateRules[number][0].getForthRedRate()) {
				// 第一与第二相同，或者与第二相同，或者与第一第二都不同
				if (maxRateRules[number][0].getRuleType().equals(maxRateRules[number][1])
						|| rule.getRuleType().equals(maxRateRules[number][0])
						|| (!rule.getRuleType().equals(maxRateRules[number][0]) && !rule
								.getRuleType().equals(maxRateRules[number][1]))) {
					maxRateRules[number][1] = maxRateRules[number][0];
				}
				maxRateRules[number][0] = rule;
			} else if (rule.getForthRedRate() > maxRateRules[number][1].getForthRedRate()) {
				if (maxRateRules[number][0].getRuleType().equals(maxRateRules[number][1])
						|| !rule.getRuleType().equals(maxRateRules[number][0])) {
					maxRateRules[number][1] = rule;
				}
			}
			// 5
			number = 4;
			if (rule.getFifthRedRate() > maxRateRules[number][0].getFifthRedRate()) {
				// 第一与第二相同，或者与第二相同，或者与第一第二都不同
				if (maxRateRules[number][0].getRuleType().equals(maxRateRules[number][1])
						|| rule.getRuleType().equals(maxRateRules[number][0])
						|| (!rule.getRuleType().equals(maxRateRules[number][0]) && !rule
								.getRuleType().equals(maxRateRules[number][1]))) {
					maxRateRules[number][1] = maxRateRules[number][0];
				}
				maxRateRules[number][0] = rule;
			} else if (rule.getFifthRedRate() > maxRateRules[number][1].getFifthRedRate()) {
				if (maxRateRules[number][0].getRuleType().equals(maxRateRules[number][1])
						|| !rule.getRuleType().equals(maxRateRules[number][0])) {
					maxRateRules[number][1] = rule;
				}
			}
			// 6
			number = 5;
			if (rule.getSixthRedRate() > maxRateRules[number][0].getSixthRedRate()) {
				// 第一与第二相同，或者与第二相同，或者与第一第二都不同
				if (maxRateRules[number][0].getRuleType().equals(maxRateRules[number][1])
						|| rule.getRuleType().equals(maxRateRules[number][0])
						|| (!rule.getRuleType().equals(maxRateRules[number][0]) && !rule
								.getRuleType().equals(maxRateRules[number][1]))) {
					maxRateRules[number][1] = maxRateRules[number][0];
				}
				maxRateRules[number][0] = rule;
			} else if (rule.getSixthRedRate() > maxRateRules[number][1].getSixthRedRate()) {
				if (maxRateRules[number][0].getRuleType().equals(maxRateRules[number][1])
						|| !rule.getRuleType().equals(maxRateRules[number][0])) {
					maxRateRules[number][1] = rule;
				}
			}
			// 7
			number = 6;
			if (rule.getBlueRate() > maxRateRules[number][0].getBlueRate()) {
				// 第一与第二相同，或者与第二相同，或者与第一第二都不同
				if (maxRateRules[number][0].getRuleType().equals(maxRateRules[number][1])
						|| rule.getRuleType().equals(maxRateRules[number][0])
						|| (!rule.getRuleType().equals(maxRateRules[number][0]) && !rule
								.getRuleType().equals(maxRateRules[number][1]))) {
					maxRateRules[number][1] = maxRateRules[number][0];
				}
				maxRateRules[number][0] = rule;
			} else if (rule.getBlueRate() > maxRateRules[number][1].getBlueRate()) {
				if (maxRateRules[number][0].getRuleType().equals(maxRateRules[number][1])
						|| !rule.getRuleType().equals(maxRateRules[number][0])) {
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
				MockPredictEntity queryById = daoService.queryById(predictId,
						MockPredictEntity.class);
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
			List<String> sortMapToList = MapUtil.sortMapToListByValue(redmap, "=", MapUtil.DESC);
			String red = sortMapToList.get(0).split("=")[0];
			min = Math.max(Integer.valueOf(red), i) + 1;
			max = Math.min(33 / 6 * (i + 3), 33 - i + 1);
			sb.append(i == 0 ? "" : ",").append(red);
		}

		Map<String, Integer> blueMap = new HashMap<String, Integer>();
		for (int j = 0; j < 2; j++) {
			String predictId = maxRateRules[6][j].getId() + "-" + nextTermNo;
			MockPredictEntity queryById = daoService.queryById(predictId, MockPredictEntity.class);
			for (int k = 0; k < blueCount; k++) {
				String blue = queryById.getBlueNumbers().get(k).split("=")[0];
				int original = blueMap.get(blue) == null ? 0 : blueMap.get(blue);
				blueMap.put(blue, original + 2 - j + blueCount - k);
			}
		}
		sb.append("/");
		List<String> redList = MapUtil.sortMapToListByValue(allRedMap, "=", MapUtil.DESC);
		min = 1;
		max = 33 / 6 * 2;
		for (int i = 0; i < 6 && i < redList.size(); i++) {
			String red = redList.get(i).split("=")[0];
			min = Math.max(Integer.valueOf(red), i) + 1;
			max = Math.min(33 / 6 * (i + 3), 33 - i + 1);
			sb.append(i == 0 ? "" : ",").append(red);
		}

		List<String> blueList = MapUtil.sortMapToListByValue(blueMap, "=", MapUtil.DESC);
		sb.append("+");
		for (int i = 0; i < blueCount && i < blueList.size(); i++) {
			String blue = blueList.get(i).split("=")[0];
			sb.append(i == 0 ? "" : ",").append(blue);
		}
		MockRecommendEntity entity = new MockRecommendEntity();
		entity.setPrimaryKey("20170316", nextTermNo);
		entity.setRecommendNumbers(sb.toString());
		entity.setRedNumbers(redList);
		entity.setBlueNumbers(blueList);

		daoService.save(entity);
	}

	public void predictByResults(List<MockResultEntity> list, List<MockRuleEntity> rules) {
		for (MockRuleEntity rule : rules) {
			rule.setExecuteStatus(ExecuteStatus.REDAY.name());
			daoService.save(rule);
		}

		List<MockPredictEntity> predicts = this.excuteBaseRules(list, rules);
		daoService.save(predicts);
		predicts = this.excuteGeneRules(predicts, rules, list.size());
		daoService.save(predicts);
	}

	private List<MockPredictEntity> excuteGeneRules(List<MockPredictEntity> basePredicts,
			List<MockRuleEntity> rules, int terms) {
		List<MockPredictEntity> result = new ArrayList<MockPredictEntity>();
		Map<String, MockPredictEntity> map = new HashMap<String, MockPredictEntity>();
		for (MockPredictEntity basePredict : basePredicts) {
			map.put(basePredict.getRuleId(), basePredict);
		}

		for (MockRuleEntity rule : rules) {
			if (!RuleType.MULTI.name().equals(rule.getRuleType()) || rule.getTerms() > terms) {
				rule.setExecuteStatus(ExecuteStatus.SUCCESS.name());
				daoService.save(rule);
				continue;
			}
			rule.setExecuteStatus(ExecuteStatus.RUNNING.name());
			daoService.save(rule);
			MockPredictEntity entity = this.excuteGeneRule(rule, map, basePredicts);
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

	private List<MockPredictEntity> excuteBaseRules(List<MockResultEntity> list,
			List<MockRuleEntity> rules) {
		List<MockPredictEntity> result = new ArrayList<MockPredictEntity>();
		for (MockRuleEntity rule : rules) {
			if (rule.getTerms() <= list.size()
					&& RuleType.DISPLAY_TIMES.name().equals(rule.getRuleType())) {
				rule.setExecuteStatus(ExecuteStatus.RUNNING.name());
				daoService.save(rule);
				MockPredictEntity predict = this.excuteDisplayTimes(list, rule);
				result.add(predict);
				rule.setExecuteStatus(ExecuteStatus.SUCCESS.name());
				daoService.save(rule);
			} else if (rule.getTerms() <= list.size()
					&& RuleType.SKIP_TIMES.name().equals(rule.getRuleType())) {
				rule.setExecuteStatus(ExecuteStatus.RUNNING.name());
				daoService.save(rule);
				MockPredictEntity predict = this.excuteSkipTimes(list, rule);
				result.add(predict);
			} else if (rule.getTerms() <= list.size()
					&& RuleType.DOUBLE_TIMES.name().equals(rule.getRuleType())) {
				rule.setExecuteStatus(ExecuteStatus.RUNNING.name());
				daoService.save(rule);
				MockPredictEntity predict = this.excuteDoubleTimes(list, rule);
				result.add(predict);
			}
		}
		return result;
	}

	private MockPredictEntity excuteGeneRule(MockRuleEntity rule,
			Map<String, MockPredictEntity> map, List<MockPredictEntity> basePredicts) {
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
			MockPredictEntity MockPredictEntity = map.get(ruleId);
			if (MockPredictEntity == null) {
				logger.info("未找到该基础规则的预测结果，请手动创建该规则:" + ruleId);
				return null;
			}
			redNumbers = addTwoList(redNumbers, MockPredictEntity.getRedNumbers());
			blueNumbers = addTwoList(blueNumbers, MockPredictEntity.getBlueNumbers());
		}

		Map<String, Integer> redMap = new HashMap<String, Integer>();
		for (String redNumber : redNumbers) {
			String[] split = redNumber.split("=");
			redMap.put(split[0], Integer.parseInt(split[1]));
		}
		redNumbers = MapUtil.sortMapToListByValue(redMap, "=", MapUtil.DESC);

		Map<String, Integer> blueMap = new HashMap<String, Integer>();
		for (String blueNumber : blueNumbers) {
			String[] split = blueNumber.split("=");
			blueMap.put(split[0], Integer.parseInt(split[1]));
		}
		blueNumbers = MapUtil.sortMapToListByValue(blueMap, "=", MapUtil.DESC);

		MockPredictEntity entity = new MockPredictEntity();
		String ruleId = rule.getId();
		String termNo = basePredicts.get(0).getTermNo();
		entity.setPrimaryKey(ruleId, termNo);
		entity.setRedNumbers(redNumbers);
		entity.setBlueNumbers(blueNumbers);
		entity.setCreateTime(new Date());
		entity.setUpdateTime(new Date());
		return entity;
	}

	public MockPredictEntity excuteDisplayTimes(List<MockResultEntity> list, MockRuleEntity entity) {
		Result countResult = this.countDisplayTimes(list, entity.getTerms());
		// 处理为负数
		Map<String, Integer> redMap = countResult.getRedMap();
		Map<String, Integer> blueMap = countResult.getBlueMap();
		for (Entry<String, Integer> entry : redMap.entrySet()) {
			entry.setValue(0 - entry.getValue());
		}
		for (Entry<String, Integer> entry : blueMap.entrySet()) {
			entry.setValue(0 - entry.getValue());
		}

		List<String> redNumbers = MapUtil.sortMapToListByValue(redMap, "=", MapUtil.DESC);
		List<String> blueNumbers = MapUtil.sortMapToListByValue(blueMap, "=", MapUtil.DESC);
		MockPredictEntity result = new MockPredictEntity();
		String ruleId = entity.getId();
		String termNo = String.valueOf(Integer.valueOf(list.get(0).getId()) + 1);
		result.setPrimaryKey(ruleId, termNo);
		result.setRedNumbers(redNumbers);
		result.setBlueNumbers(blueNumbers);
		result.setCreateTime(new Date());
		result.setUpdateTime(new Date());
		return result;
	}

	public Result countDisplayTimes(List<MockResultEntity> list, int terms) {
		if (terms > list.size()) {
			terms = list.size();
		}
		Map<String, Integer> redMap = this.initMapKeysWithValue(SsqConstant.RED_NUMBERS, 0);
		Map<String, Integer> blueMap = this.initMapKeysWithValue(SsqConstant.BLUE_NUMBERS, 0);
		int redCount = 0;
		int blueCount = 0;
		for (int i = 0; i < terms; i++) {
			MockResultEntity lotterySsqEntity = list.get(i);
			String[] redNumbers = lotterySsqEntity.getReds().split(",");
			for (String redNumber : redNumbers) {
				redMap.put(redNumber, redMap.get(redNumber) + 1);
				redCount++;
			}
			String blueNumber = lotterySsqEntity.getBlue();
			blueMap.put(blueNumber, blueMap.get(blueNumber) + 1);
			blueCount++;
		}
		Result result = new Result();
		result.setRedMap(redMap);
		result.setBlueMap(blueMap);
		result.setRedCount(redCount);
		result.setBlueCount(blueCount);
		return result;
	}

	public MockPredictEntity excuteDoubleTimes(List<MockResultEntity> list, MockRuleEntity entity) {
		Result countResult = this.countDoubleTimes(list, entity.getTerms());
		Map<String, Integer> redMap = countResult.getRedMap();
		Map<String, Integer> blueMap = countResult.getBlueMap();

		// 按上期出现的号码，处理为负数
		MockResultEntity MockResultEntity = list.get(0);
		List<String> lastRedNumbers = Arrays.asList(MockResultEntity.getReds().split(","));
		String lastBlueNumber = MockResultEntity.getBlue().split(",")[0];
		for (Entry<String, Integer> entry : redMap.entrySet()) {
			if (lastRedNumbers.contains(entry.getKey())) {
				entry.setValue(0 - entry.getValue());
			} else {
				entry.setValue(0);
			}
		}
		for (Entry<String, Integer> entry : blueMap.entrySet()) {
			if (lastBlueNumber.equals(entry.getKey())) {
				entry.setValue(0 - entry.getValue());
			} else {
				entry.setValue(0);
			}
		}

		List<String> redNumbers = MapUtil.sortMapToListByValue(redMap, "=", MapUtil.DESC);
		List<String> blueNumbers = MapUtil.sortMapToListByValue(blueMap, "=", MapUtil.DESC);

		MockPredictEntity result = new MockPredictEntity();
		String ruleId = entity.getId();
		String termNo = String.valueOf(Integer.valueOf(list.get(0).getId()) + 1);
		result.setPrimaryKey(ruleId, termNo);
		result.setRedNumbers(redNumbers);
		result.setBlueNumbers(blueNumbers);
		result.setCreateTime(new Date());
		result.setUpdateTime(new Date());
		return result;
	}

	public Result countDoubleTimes(List<MockResultEntity> list, int count) {
		if (count >= list.size()) {
			count = list.size();
		}
		Map<String, Integer> redMap = initMapKeysWithValue(SsqConstant.RED_NUMBERS, 0);
		Map<String, Integer> blueMap = initMapKeysWithValue(SsqConstant.BLUE_NUMBERS, 0);
		Result result = new Result();
		List<String> lastRedNumbers = new ArrayList<String>();
		String lastBlueNumber = null;
		int redCount = 0;
		int blueCount = 0;
		for (int i = 0; i < count; i++) {
			MockResultEntity MockResultEntity = list.get(i);
			String[] redNumbers = MockResultEntity.getReds().split(",");
			for (String redNumber : redNumbers) {
				if (lastRedNumbers.contains(redNumber)) {
					redMap.put(redNumber, redMap.get(redNumber) + 1);
					redCount++;
				}
			}
			lastRedNumbers = Arrays.asList(redNumbers);
			// 去除幸运号
			String blueNumber = MockResultEntity.getBlue();
			if (blueNumber.equals(lastBlueNumber)) {
				blueMap.put(blueNumber, blueMap.get(blueNumber) + 1);
				blueCount++;
			}
			lastBlueNumber = blueNumber;
		}
		result.setRedMap(redMap);
		result.setBlueMap(blueMap);
		result.setRedCount(redCount);
		result.setBlueCount(blueCount);
		return result;
	}

	public MockPredictEntity excuteSkipTimes(List<MockResultEntity> list, MockRuleEntity entity) {
		Result countResult = this.countSkipTimes(list, entity.getTerms());
		List<String> redNumbers = MapUtil.sortMapToListByValue(countResult.getRedMap(), "=", MapUtil.DESC);
		List<String> blueNumbers = MapUtil.sortMapToListByValue(countResult.getBlueMap(), "=",
				MapUtil.DESC);

		MockPredictEntity result = new MockPredictEntity();
		String ruleId = entity.getId();
		String termNo = String.valueOf(Integer.valueOf(list.get(0).getId()) + 1);
		result.setPrimaryKey(ruleId, termNo);
		result.setRedNumbers(redNumbers);
		result.setBlueNumbers(blueNumbers);
		result.setCreateTime(new Date());
		result.setUpdateTime(new Date());
		return result;
	}

	public Result countSkipTimes(List<MockResultEntity> list, int count) {
		if (count >= list.size()) {
			count = list.size();
		}
		Map<String, Integer> redMap = initMapKeysWithValue(SsqConstant.RED_NUMBERS, count);
		Map<String, Integer> blueMap = initMapKeysWithValue(SsqConstant.BLUE_NUMBERS, count);

		for (int i = 0; i < count; i++) {
			MockResultEntity MockResultEntity = list.get(i);
			String[] redNumbers = MockResultEntity.getReds().split(",");
			for (String redNumber : redNumbers) {
				if (!Arrays.asList(SsqConstant.RED_NUMBERS).contains(redNumber)) {
					logger.error("数据错误，lotterySsqEntity:" + MockResultEntity);
				}
				if (redMap.get(redNumber) == count) {
					redMap.put(redNumber, i);
				}
			}
			String blueNumber = MockResultEntity.getBlue();
			if (blueMap.get(blueNumber) == count) {
				blueMap.put(blueNumber, i);
			}
		}
		Result result = new Result();
		result.setRedMap(redMap);
		result.setBlueMap(blueMap);
		return result;
	}

	private Map<String, Integer> initMapKeysWithValue(String[] keys, int value) {
		Map<String, Integer> map = new HashMap<String, Integer>();
		for (String key : keys) {
			map.put(key, value);
		}
		return map;
	}

	public String getRightNumPositions(MockPredictEntity mockPredict, MockResultEntity mockResult) {
		String[] redNumbers = mockResult.getReds().split(",");
		StringBuilder rightPoses = new StringBuilder();
		for (String rightRedNumber : redNumbers) {
			for (int i = 0; i < mockPredict.getRedNumbers().size(); i++) {
				String predictRedNumber = mockPredict.getRedNumbers().get(i);
				if (rightRedNumber.equals(predictRedNumber.split("=")[0])) {
					rightPoses.append(",").append(i + 1);
					break;
				}
			}
		}
		rightPoses.append("+");
		String rightBlueNumber = mockResult.getBlue();
		for (int i = 0; i < mockPredict.getBlueNumbers().size(); i++) {
			String predictBlueNumber = mockPredict.getBlueNumbers().get(i);
			if (rightBlueNumber.equals(predictBlueNumber.split("=")[0])) {
				rightPoses.append(i + 1);
				break;
			}
		}
		return rightPoses.substring(1);
	}

	public MockRuleEntity countRate(MockRuleEntity mockRule, int redCount, int blueCount) {
		Condition condition = new Condition();
		condition.addParam("ruleId", "=", mockRule.getId());
		List<SsqPredictEntity> ssqPredicts = daoService.query(condition, SsqPredictEntity.class);
		Double firstRate = 0D;// 一等奖准确率
		Double secondRate = 0D;// 二等奖准确率
		Double firstRedRate = 0D;// 1号球准确率
		Double secondRedRate = 0D;// 2号球准确率
		Double thirdRedRate = 0D;// 3号球准确率
		Double forthRedRate = 0D;// 4号球准确率
		Double fifthRedRate = 0D;// 5号球准确率
		Double sixthRedRate = 0D;// 6号球准确率
		Double blueRate = 0D;// 蓝球准确率
		int count = 0;
		for (SsqPredictEntity ssqPredict : ssqPredicts) {
			String rightNumbers = ssqPredict.getRightNumbers();
			if (StringUtils.isEmpty(rightNumbers)) {
				continue;
			}
			count++;
			int rightRedCount = 0;
			int rightBlueCount = 0;
			String[] split = rightNumbers.split("\\+");
			String[] rightReds = split[0].split(",");
			String rightBlue = split[1];
			for (int i = 0; i < rightReds.length; i++) {
				if (Integer.valueOf(rightReds[i]) <= redCount) {
					if (i == 0) {
						firstRedRate++;
					} else if (i == 1) {
						secondRedRate++;
					} else if (i == 2) {
						thirdRedRate++;
					} else if (i == 3) {
						forthRedRate++;
					} else if (i == 4) {
						fifthRedRate++;
					} else if (i == 5) {
						sixthRedRate++;
					}
					rightRedCount++;
				}
			}
			if (Integer.valueOf(rightBlue) <= blueCount) {
				rightBlueCount++;
				blueRate++;
			}
			String con = rightRedCount + "+" + rightBlueCount;
			if ("6+1".equals(con)) {
				firstRate++;
			} else if ("6+0".equals(con)) {
				secondRate++;
			}
		}
		// 注数
		BigInteger number = MathUtil.C(new BigInteger(String.valueOf(redCount)),
				new BigInteger("6")).multiply(
				MathUtil.C(new BigInteger(String.valueOf(redCount)), new BigInteger("6")));
		mockRule.setFirstRate(firstRate / count / number.intValue());
		mockRule.setSecondRate(secondRate / count / number.intValue());
		mockRule.setFirstRedRate(firstRedRate);
		mockRule.setSecondRedRate(secondRedRate);
		mockRule.setThirdRedRate(thirdRedRate);
		mockRule.setForthRedRate(forthRedRate);
		mockRule.setFifthRedRate(fifthRedRate);
		mockRule.setSixthRedRate(sixthRedRate);
		mockRule.setBlueRate(blueRate);
		return mockRule;
	}
}

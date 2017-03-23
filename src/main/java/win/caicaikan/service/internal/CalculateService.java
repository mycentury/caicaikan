/**
 * 
 */
package win.caicaikan.service.internal;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import win.caicaikan.repository.mongodb.entity.PredictRuleEntity;
import win.caicaikan.repository.mongodb.entity.PrizeRuleEntity;
import win.caicaikan.repository.mongodb.entity.ssq.SsqPredictEntity;
import win.caicaikan.repository.mongodb.entity.ssq.SsqResultEntity;
import win.caicaikan.service.internal.DaoService.Condition;
import win.caicaikan.util.MapUtil;
import win.caicaikan.util.MathUtil;
import win.caicaikan.util.MathUtil.GaussianParam;

/**
 * @Desc
 * @author wewenge.yan
 * @Date 2017年3月15日
 * @ClassName CalculateService
 */
@Service
public class CalculateService implements InitializingBean {
	@Autowired
	private DaoService daoService;

	private Map<String, PrizeRuleEntity> prizeRuleMap;

	public void initPrizeRuleMap() {
		prizeRuleMap = new HashMap<String, PrizeRuleEntity>();
		List<PrizeRuleEntity> allPrizeRules = daoService.query(null, PrizeRuleEntity.class);
		for (PrizeRuleEntity prizeRule : allPrizeRules) {
			prizeRuleMap.put(prizeRule.getId(), prizeRule);
		}
	}

	/**
	 * 获取排列组合结果count值List
	 * 
	 * @param lastNumbers
	 * @param lastCounts
	 * @param seq
	 * @param times
	 * @param n
	 * @return
	 */
	public List<String> getNumberList(List<String> lastNumbers, List<String> lastCounts, int seq, int times, int n) {
		if (seq > times) {
			return lastCounts;
		}
		List<String> thisNumbers = new ArrayList<String>();
		List<String> thisCounts = new ArrayList<String>();
		if (CollectionUtils.isEmpty(lastNumbers) && CollectionUtils.isEmpty(lastCounts) && seq == 1) {
			for (int i = 1; i <= Math.min((n - times) + 1, n); i++) {
				thisNumbers.add(String.valueOf(i));
				thisCounts.add(String.valueOf(i));
			}
		} else {
			for (int i = 0; i < lastCounts.size(); i++) {
				String number = lastNumbers.get(i);
				String count = lastCounts.get(i);
				for (int j = Integer.valueOf(number) + 1; j <= Math.min(n - times + seq + 1, n); j++) {
					thisNumbers.add(String.valueOf(j));
					thisCounts.add(String.valueOf(Integer.valueOf(count) + j));
				}
			}
		}
		if (seq < times) {
			thisCounts = this.getNumberList(thisNumbers, thisCounts, seq + 1, times, n);
		}
		return thisCounts;
	}

	public void countRate() {
		List<SsqResultEntity> results = daoService.query(null, SsqResultEntity.class);
		String[][] numbers = new String[8][results.size()];
		Map<String, Integer> countRedsMap = new HashMap<String, Integer>();
		Map<Integer, Integer> countAvgMap = new HashMap<Integer, Integer>();
		Map<String, Integer> countBlueMap = new HashMap<String, Integer>();
		for (int i = 0; i < results.size(); i++) {
			SsqResultEntity result = results.get(i);
			if (StringUtils.isEmpty(result.getRedNumbers()) || StringUtils.isEmpty(result.getBlueNumbers())) {
				continue;
			}
			String[] split = result.getRedNumbers().split(",");
			if (split.length < 6) {
				continue;
			}
			Integer avg = 0;
			for (int j = 0; j < 6; j++) {
				numbers[j][i] = split[j];
				avg += Integer.valueOf(split[j]);
				Integer count = countRedsMap.get(split[j]);
				countRedsMap.put(split[j], count == null ? 1 : (count + 1));
			}
			numbers[6][i] = String.valueOf(avg);
			Integer countAvg = countAvgMap.get(avg);
			countAvgMap.put(avg, (countAvg == null ? 0 : countAvg) + 1);

			split = result.getBlueNumbers().split(",");
			numbers[7][i] = split[0];
			Integer count = countBlueMap.get(split[0]);
			countBlueMap.put(split[0], count == null ? 1 : (count + 1));
		}

		Map<String, Double> countRedsByGS = new HashMap<String, Double>();
		GaussianParam gaussianParam = null;
		for (int i = 0; i < 6; i++) {
			gaussianParam = MathUtil.calculateGaussianParam(numbers[i]);
			for (int j = i + 1; j < 27 + i + 1 + 1; j++) {
				Double rate = MathUtil.calculateGaussianDistribution(gaussianParam, (double) j);
				String key = (j < 10 ? "0" : "") + String.valueOf(j);
				Double count = countRedsByGS.get(key);
				countRedsByGS.put(key, count == null ? rate : (rate + count));
				System.out.println("第" + (i + 1) + "个号出现" + j + "的概率为：" + rate);
			}
		}
		List<String> redListByGS = MapUtil.sortMapToListByKey(countRedsByGS, "=", MapUtil.ASC);
		List<String> redListByCount = MapUtil.sortMapToListByKey(countRedsMap, "=", MapUtil.ASC);
		System.out.println("红号高斯计算为：" + redListByGS);
		System.out.println("红号统计为：" + redListByCount);
		gaussianParam = MathUtil.calculateGaussianParam(numbers[6]);
		Map<Integer, Double> avgCalRate = new HashMap<Integer, Double>();
		for (Integer j = (1 + 6) * 3; j < (28 + 33) * 3; j++) {
			Double rate = MathUtil.calculateGaussianDistribution(gaussianParam, (double) j);
			System.out.println("红号平均出现" + j + "的概率为：" + rate);
			avgCalRate.put(j, rate);
		}
		List<String> sortMapToList = MapUtil.sortMapToListByKey(avgCalRate, "=", MapUtil.ASC);
		System.out.println("红号平均高斯计算为：" + sortMapToList);
		sortMapToList = MapUtil.sortMapToListByKey(countAvgMap, "=", MapUtil.ASC);
		System.out.println("红号平均统计为：" + sortMapToList);
		gaussianParam = MathUtil.calculateGaussianParam(numbers[7]);
		for (int j = 1; j < 17; j++) {
			Double rate = MathUtil.calculateGaussianDistribution(gaussianParam, (double) j);
			System.out.println("蓝号出现" + j + "的概率为：" + rate);
		}
		sortMapToList = MapUtil.sortMapToListByKey(countBlueMap, "=", MapUtil.ASC);
		System.out.println("蓝号统计为：" + sortMapToList);
	}

	/**
	 * @param predictRuleId 规则ID
	 * @param redCount 红球数
	 * @param blueCount 蓝球数
	 * @return
	 */
	public PredictRuleEntity countRate(PredictRuleEntity predictRule, int redCount, int blueCount) {
		Condition condition = new Condition();
		condition.addParam("ruleId", "=", predictRule.getId());
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
		BigInteger number = MathUtil.C(new BigInteger(String.valueOf(redCount)), new BigInteger("6")).multiply(
				MathUtil.C(new BigInteger(String.valueOf(redCount)), new BigInteger("6")));
		predictRule.setFirstRate(firstRate / count / number.intValue());
		predictRule.setSecondRate(secondRate / count / number.intValue());
		predictRule.setFirstRedRate(firstRedRate);
		predictRule.setSecondRedRate(secondRedRate);
		predictRule.setThirdRedRate(thirdRedRate);
		predictRule.setForthRedRate(forthRedRate);
		predictRule.setFifthRedRate(fifthRedRate);
		predictRule.setSixthRedRate(sixthRedRate);
		predictRule.setBlueRate(blueRate);
		return predictRule;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		this.initPrizeRuleMap();
	}
}

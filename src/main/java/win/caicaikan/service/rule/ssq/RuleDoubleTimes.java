/**
 * 
 */
package win.caicaikan.service.rule.ssq;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.stereotype.Service;

import win.caicaikan.constant.RuleType;
import win.caicaikan.constant.SsqConstant;
import win.caicaikan.repository.mongodb.entity.PredictRuleEntity;
import win.caicaikan.repository.mongodb.entity.ssq.SsqPredictEntity;
import win.caicaikan.repository.mongodb.entity.ssq.SsqResultEntity;
import win.caicaikan.service.rule.RuleTemplate;
import win.caicaikan.util.MapUtil;

/**
 * @Desc
 * @author wewenge.yan
 * @Date 2016年11月21日
 * @ClassName RuleDoubleTimes
 */
@Service
public class RuleDoubleTimes extends RuleTemplate {

	@Override
	public RuleType getRuleType() {
		return RuleType.DOUBLE_TIMES;
	}

	@Override
	public SsqPredictEntity excute(List<SsqResultEntity> list, PredictRuleEntity entity) throws Throwable {
		Result countResult = this.countDoubleTimes(list, entity.getTerms());
		Map<String, Integer> redMap = countResult.getRedMap();
		Map<String, Integer> blueMap = countResult.getBlueMap();

		// 按上期出现的号码，处理为负数
		SsqResultEntity ssqResultEntity = list.get(0);
		List<String> lastRedNumbers = Arrays.asList(ssqResultEntity.getRedNumbers().split(","));
		// 去除幸运号
		String lastBlueNumber = ssqResultEntity.getBlueNumbers().split(",")[0];
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

		List<String> redNumbers = MapUtil.sortMapToList(redMap, "=", MapUtil.DESC);
		List<String> blueNumbers = MapUtil.sortMapToList(blueMap, "=", MapUtil.DESC);

		SsqPredictEntity result = new SsqPredictEntity();
		String ruleId = entity.getId();
		String termNo = ruleService.getNextTermNoOfSsq(list.get(0));
		result.setPrimaryKey(ruleId, termNo);
		result.setRedNumbers(redNumbers);
		result.setBlueNumbers(blueNumbers);
		result.setCreateTime(new Date());
		result.setUpdateTime(new Date());
		return result;
	}

	public Result countDoubleTimes(List<SsqResultEntity> list, int count) {
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
			SsqResultEntity ssqResultEntity = list.get(i);
			String[] redNumbers = ssqResultEntity.getRedNumbers().split(",");
			for (String redNumber : redNumbers) {
				if (lastRedNumbers.contains(redNumber)) {
					redMap.put(redNumber, redMap.get(redNumber) + 1);
					redCount++;
				}
			}
			lastRedNumbers = Arrays.asList(redNumbers);
			// 去除幸运号
			String blueNumber = ssqResultEntity.getBlueNumbers().split(",")[0];
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
}
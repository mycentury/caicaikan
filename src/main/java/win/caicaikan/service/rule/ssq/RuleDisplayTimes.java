/**
 * 
 */
package win.caicaikan.service.rule.ssq;

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
 * @ClassName RuleDisplayTimes
 */
@Service
public class RuleDisplayTimes extends RuleTemplate {

	@Override
	public RuleType getRuleType() {
		return RuleType.DISPLAY_TIMES;
	}

	@Override
	public SsqPredictEntity excute(List<SsqResultEntity> list, PredictRuleEntity entity) {
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

		List<String> redNumbers = MapUtil.sortMapToList(redMap, "=", MapUtil.DESC);
		List<String> blueNumbers = MapUtil.sortMapToList(blueMap, "=", MapUtil.DESC);
		SsqPredictEntity result = new SsqPredictEntity();
		String ruleId = entity.getId();
		String termNo = null;
		termNo = ruleService.getNextTermNoOfSsq(list.get(0));
		result.setPrimaryKey(ruleId, termNo);
		result.setRedNumbers(redNumbers);
		result.setBlueNumbers(blueNumbers);
		result.setCreateTime(new Date());
		result.setUpdateTime(new Date());
		return result;
	}

	public Result countDisplayTimes(List<SsqResultEntity> list, int terms) {
		if (terms > list.size()) {
			terms = list.size();
		}
		Map<String, Integer> redMap = this.initMapKeysWithValue(SsqConstant.RED_NUMBERS, 0);
		Map<String, Integer> blueMap = this.initMapKeysWithValue(SsqConstant.BLUE_NUMBERS, 0);
		int redCount = 0;
		int blueCount = 0;
		for (int i = 0; i < terms; i++) {
			SsqResultEntity lotterySsqEntity = list.get(i);
			String[] redNumbers = lotterySsqEntity.getRedNumbers().split(",");
			for (String redNumber : redNumbers) {
				redMap.put(redNumber, redMap.get(redNumber) + 1);
				redCount++;
			}
			// 去除幸运号
			String blueNumber = lotterySsqEntity.getBlueNumbers().split(",")[0];
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
}
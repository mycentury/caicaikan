/**
 * 
 */
package win.caicaikan.service.rule.ssq;

import java.util.Date;
import java.util.List;
import java.util.Map;

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
	public SsqPredictEntity excute(List<SsqResultEntity> list, PredictRuleEntity entity)
			throws Throwable {

		Result countResult = this.countDisplayTimes(list, entity.getTerms());
		Map<String, Integer> redMap = countResult.getRedMap();
		Map<String, Integer> blueMap = countResult.getBlueMap();
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

	public Result countDisplayTimes(List<SsqResultEntity> list, int count) {
		if (count >= list.size()) {
			count = list.size();
		}
		Map<String, Integer> redMap = initMapKeysWithValue(SsqConstant.RED_NUMBERS, 0);
		Map<String, Integer> blueMap = initMapKeysWithValue(SsqConstant.BLUE_NUMBERS, 0);
		Result result = new Result();
		for (int i = 0; i < count; i++) {
			SsqResultEntity lotterySsqEntity = list.get(i);
			String[] redNumbers = lotterySsqEntity.getRedNumbers().split(",");
			for (String redNumber : redNumbers) {
				redMap.put(redNumber, redMap.get(redNumber) + 1);
			}
			// 去除幸运号
			String blueNumber = lotterySsqEntity.getBlueNumbers().split(",")[0];
			blueMap.put(blueNumber, blueMap.get(blueNumber) + 1);
		}
		result.setRedMap(redMap);
		result.setBlueMap(blueMap);
		return result;
	}
}
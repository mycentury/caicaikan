/**
 * 
 */
package win.caicaikan.service.rule.ssq;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
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
public class RuleSkipTimes extends RuleTemplate {
	private final static Logger logger = Logger.getLogger(RuleSkipTimes.class);

	@Override
	public RuleType getRuleType() {
		return RuleType.SKIP_TIMES;
	}

	@Override
	public SsqPredictEntity excute(List<SsqResultEntity> list, PredictRuleEntity entity) throws Throwable {
		Result countResult = countSkipTimes(list, entity.getTerms());
		List<String> redNumbers = MapUtil.sortMapToList(countResult.getRedMap(), "=", MapUtil.DESC);
		List<String> blueNumbers = MapUtil.sortMapToList(countResult.getBlueMap(), "=", MapUtil.DESC);

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

	public Result countSkipTimes(List<SsqResultEntity> list, int count) {
		if (count >= list.size()) {
			count = list.size();
		}
		Map<String, Integer> redMap = initMapKeysWithValue(SsqConstant.RED_NUMBERS, count);
		Map<String, Integer> blueMap = initMapKeysWithValue(SsqConstant.BLUE_NUMBERS, count);

		for (int i = 0; i < count; i++) {
			SsqResultEntity ssqResultEntity = list.get(i);
			String[] redNumbers = ssqResultEntity.getRedNumbers().split(",");
			for (String redNumber : redNumbers) {
				if (!Arrays.asList(SsqConstant.RED_NUMBERS).contains(redNumber)) {
					logger.error("数据错误，lotterySsqEntity:" + ssqResultEntity);
				}
				if (redMap.get(redNumber) == count) {
					redMap.put(redNumber, i);
				}
			}
			String blueNumber = ssqResultEntity.getBlueNumbers().split(",")[0];
			if (blueMap.get(blueNumber) == count) {
				blueMap.put(blueNumber, i);
			}
		}
		Result result = new Result();
		result.setRedMap(redMap);
		result.setBlueMap(blueMap);
		return result;
	}
}
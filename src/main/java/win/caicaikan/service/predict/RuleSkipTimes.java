/**
 * 
 */
package win.caicaikan.service.predict;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import win.caicaikan.constant.LotteryType;
import win.caicaikan.constant.Rule;
import win.caicaikan.constant.SsqConstant;
import win.caicaikan.repository.mongodb.entity.LotteryPredictEntity;
import win.caicaikan.repository.mongodb.entity.LotteryRuleEntity;
import win.caicaikan.repository.mongodb.entity.LotterySsqEntity;
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
	public Rule getRule() {
		return Rule.SSQ_0_003;
	}

	@Override
	public LotteryPredictEntity excute(List<LotterySsqEntity> list, LotteryRuleEntity entity) throws Throwable {
		Map<Integer, Integer> periodsAndweights = entity.getPeriodsAndweights();
		Map<String, Integer> redMap = initMapKeysWithValue(SsqConstant.RED_NUMBERS, 0);
		Map<String, Integer> blueMap = initMapKeysWithValue(SsqConstant.BLUE_NUMBERS, 0);
		for (Entry<Integer, Integer> entry : periodsAndweights.entrySet()) {
			int max = entry.getKey() == 0 || entry.getKey() > list.size() ? list.size() : entry.getKey();
			Map<String, Integer> tempRedMap = initMapKeysWithValue(SsqConstant.RED_NUMBERS, max);
			Map<String, Integer> tempBlueMap = initMapKeysWithValue(SsqConstant.BLUE_NUMBERS, max);
			for (int i = 0; i < list.size(); i++) {
				LotterySsqEntity lotterySsqEntity = list.get(i);
				String[] redNumbers = lotterySsqEntity.getRedNumbers().split(",");
				for (String redNumber : redNumbers) {
					if (!Arrays.asList(SsqConstant.RED_NUMBERS).contains(redNumber)) {
						logger.error("数据错误，lotterySsqEntity:" + lotterySsqEntity);
					}
					if (i < max && tempRedMap.get(redNumber) == max) {
						tempRedMap.put(redNumber, i);
					}
				}
				String blueNumber = lotterySsqEntity.getBlueNumber().split(" ")[0];
				if ((i < max) && tempBlueMap.get(blueNumber) == max) {
					tempBlueMap.put(blueNumber, i);
				}
			}
			for (Entry<String, Integer> tempEntry : tempRedMap.entrySet()) {
				String key = tempEntry.getKey();
				Integer value = tempEntry.getValue() * entry.getValue() + redMap.get(key);
				redMap.put(key, value);
			}
			for (Entry<String, Integer> tempEntry : tempBlueMap.entrySet()) {
				String key = tempEntry.getKey();
				Integer value = tempEntry.getValue() * entry.getValue() + tempBlueMap.get(key);
				tempBlueMap.put(key, value);
			}

		}
		StringBuilder numbers = new StringBuilder();
		List<Entry<String, Integer>> redEntries = MapUtil.sortToListByValue(redMap, MapUtil.DESC);
		for (int i = 0; i < redEntries.size(); i++) {
			Entry<String, Integer> entry = redEntries.get(i);
			numbers.append(entry.getKey());
			if (entity.getRedCount() != 0 && i >= entity.getRedCount() - 1) {
				break;
			}
			numbers.append(",");
		}
		if (numbers.toString().endsWith(",")) {
			numbers = new StringBuilder(numbers.substring(0, numbers.length() - 1));
		}
		numbers.append("+");
		List<Entry<String, Integer>> blueEntries = MapUtil.sortToListByValue(blueMap, MapUtil.DESC);
		for (int i = 0; i < blueEntries.size(); i++) {
			Entry<String, Integer> entry = blueEntries.get(i);
			numbers.append(entry.getKey());
			if (entity.getBlueCount() != 0 && i >= entity.getBlueCount() - 1) {
				break;
			}
			numbers.append(",");
		}
		if (numbers.toString().endsWith(",")) {
			numbers = new StringBuilder(numbers.substring(0, numbers.length() - 1));
		}

		LotteryPredictEntity result = new LotteryPredictEntity();
		String lotteryType = LotteryType.SSQ.getCode();
		String ruleNo = getRule().getRuleNo();
		String termNo = getNextTermNo(list.get(0));
		result.setPrimaryKey(lotteryType, ruleNo, termNo);
		result.setNumbers(numbers.toString());
		result.setCreateTime(new Date());
		result.setUpdateTime(new Date());
		return result;
	}
}
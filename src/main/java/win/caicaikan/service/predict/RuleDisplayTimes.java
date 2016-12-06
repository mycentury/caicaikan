/**
 * 
 */
package win.caicaikan.service.predict;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

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
 * @ClassName RuleDisplayTimes
 */
@Service
public class RuleDisplayTimes extends RuleTemplate {

	@Override
	public Rule getRule() {
		return Rule.SSQ_0_001;
	}

	@Override
	public LotteryPredictEntity excute(List<LotterySsqEntity> list, LotteryRuleEntity entity) throws Throwable {
		Map<Integer, Integer> periodsAndweights = entity.getPeriodsAndweights();
		Map<String, Integer> redMap = initMapKeysWithValue(SsqConstant.RED_NUMBERS, 0);
		Map<String, Integer> blueMap = initMapKeysWithValue(SsqConstant.BLUE_NUMBERS, 0);
		for (int i = 0; i < list.size(); i++) {
			LotterySsqEntity lotterySsqEntity = list.get(i);
			String[] redNumbers = lotterySsqEntity.getRedNumbers().split(",");
			for (String redNumber : redNumbers) {
				for (Entry<Integer, Integer> entry : periodsAndweights.entrySet()) {
					Integer key = (entry.getKey() == 0 || entry.getKey() > list.size()) ? list.size() : entry.getKey();
					Integer value = entry.getValue();
					if (i < key) {
						redMap.put(redNumber, redMap.get(redNumber) + value);
					}
				}
			}
			String blueNumber = lotterySsqEntity.getBlueNumber().split(" ")[0];
			for (Entry<Integer, Integer> entry : periodsAndweights.entrySet()) {
				Integer key = entry.getKey();
				Integer value = entry.getValue();
				if (i < key) {
					blueMap.put(blueNumber, blueMap.get(blueNumber) + value);
				}
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
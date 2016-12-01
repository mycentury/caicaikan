package win.caicaikan.service.predict;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import win.caicaikan.constant.Rule;
import win.caicaikan.repository.mongodb.entity.LotteryPredictEntity;
import win.caicaikan.repository.mongodb.entity.LotteryRuleEntity;
import win.caicaikan.repository.mongodb.entity.LotterySsqEntity;

public abstract class RuleTemplate {
	abstract List<LotteryPredictEntity> excuteRule(List<LotterySsqEntity> list, LotteryRuleEntity rule) throws Throwable;

	abstract Rule getRule();

	protected Map<String, Integer> initMapKeysWithValue(String[] keys, int value) {
		Map<String, Integer> map = new HashMap<String, Integer>();
		for (String key : keys) {
			map.put(key, value);
		}
		return map;
	}
}

package win.caicaikan.service.rule;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import lombok.Data;
import lombok.EqualsAndHashCode;

import org.springframework.beans.factory.annotation.Autowired;

import win.caicaikan.constant.RuleType;
import win.caicaikan.repository.mongodb.entity.PredictRuleEntity;
import win.caicaikan.repository.mongodb.entity.ssq.SsqPredictEntity;
import win.caicaikan.repository.mongodb.entity.ssq.SsqResultEntity;
import win.caicaikan.util.MapUtil;

public abstract class RuleTemplate {

	@Autowired
	protected RuleService ruleService;

	public abstract SsqPredictEntity excute(List<SsqResultEntity> list, PredictRuleEntity entity)
			throws Throwable;

	public abstract RuleType getRuleType();

	protected Map<String, Integer> initMapKeysWithValue(String[] keys, int value) {
		Map<String, Integer> map = new HashMap<String, Integer>();
		for (String key : keys) {
			map.put(key, value);
		}
		return map;
	}

	@Data
	@EqualsAndHashCode(callSuper = false)
	public static class Result {
		Map<String, Integer> redMap;
		Map<String, Integer> blueMap;
	}

}

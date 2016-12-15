package win.caicaikan.service.rule;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import win.caicaikan.constant.RuleType;
import win.caicaikan.repository.mongodb.entity.PredictRuleEntity;
import win.caicaikan.repository.mongodb.entity.ssq.SsqPredictEntity;
import win.caicaikan.repository.mongodb.entity.ssq.SsqResultEntity;
import win.caicaikan.util.DateUtil;
import win.caicaikan.util.MapUtil;

public abstract class RuleTemplate {
	private static final String START_NO = "001";
	private static final String SPLITOR = "-";

	public abstract SsqPredictEntity excute(List<SsqResultEntity> list, PredictRuleEntity entity) throws Throwable;

	public abstract RuleType getRuleType();

	protected Map<String, Integer> initMapKeysWithValue(String[] keys, int value) {
		Map<String, Integer> map = new HashMap<String, Integer>();
		for (String key : keys) {
			map.put(key, value);
		}
		return map;
	}

	protected String getNextTermNo(SsqResultEntity entity) throws ParseException {
		Date thisDate = DateUtil._SECOND.parse(entity.getOpenTime());
		int yearOfThisTerm = DateUtil.getYear(thisDate);
		int week = DateUtil.getWeek(thisDate);

		int daysToNextTerm = 2;
		// 周四到下期要三天
		if (week == Calendar.THURSDAY) {
			daysToNextTerm++;
		}
		Date nextDate = DateUtil.addDays(thisDate, daysToNextTerm);
		int yearOfNextTerm = DateUtil.getYear(nextDate);
		if (yearOfNextTerm != yearOfThisTerm) {
			return yearOfNextTerm + START_NO;
		} else {
			return String.valueOf(Integer.valueOf(entity.getTermNo()) + 1);
		}
	}

	/**
	 * @param redMap
	 * @return
	 */
	protected List<String> sortMapToList(Map<String, Integer> redMap) {
		List<String> redNumbers = new ArrayList<String>();
		List<Entry<String, Integer>> redEntries = MapUtil.sortToListByValue(redMap, MapUtil.DESC);
		for (Entry<String, Integer> entry : redEntries) {
			redNumbers.add(entry.getKey() + SPLITOR + entry.getValue());
		}
		return redNumbers;
	}

}

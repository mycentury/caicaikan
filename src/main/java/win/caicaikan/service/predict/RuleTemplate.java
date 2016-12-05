package win.caicaikan.service.predict;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import win.caicaikan.constant.Rule;
import win.caicaikan.repository.mongodb.entity.LotteryPredictEntity;
import win.caicaikan.repository.mongodb.entity.LotteryRuleEntity;
import win.caicaikan.repository.mongodb.entity.LotterySsqEntity;
import win.caicaikan.util.DateUtil;

public abstract class RuleTemplate {
	private static final String START_NO = "001";

	public abstract LotteryPredictEntity excute(List<LotterySsqEntity> list,
			LotteryRuleEntity entity) throws Throwable;

	public abstract Rule getRule();

	protected Map<String, Integer> initMapKeysWithValue(String[] keys, int value) {
		Map<String, Integer> map = new HashMap<String, Integer>();
		for (String key : keys) {
			map.put(key, value);
		}
		return map;
	}

	protected String getNextTermNo(LotterySsqEntity entity) throws ParseException {
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

}

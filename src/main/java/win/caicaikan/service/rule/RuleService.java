package win.caicaikan.service.rule;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

import org.springframework.stereotype.Service;

import win.caicaikan.repository.mongodb.entity.ssq.SsqResultEntity;
import win.caicaikan.util.DateUtil;

@Service
public class RuleService {
	private String START_NO = "001";

	public String getNextTermNoOfSsq(SsqResultEntity entity) throws ParseException {
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

	public Date getNextTermOpenDateOfSsq(String openTime) throws ParseException {
		Date thisDate = DateUtil._SECOND.parse(openTime);
		int week = DateUtil.getWeek(thisDate);

		int daysToNextTerm = 2;
		// 周四到下期要三天
		if (week == Calendar.THURSDAY) {
			daysToNextTerm++;
		}
		Date nextDate = DateUtil.addDays(thisDate, daysToNextTerm);
		return nextDate;
	}
}

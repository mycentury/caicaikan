package win.caicaikan.service.rule;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import win.caicaikan.constant.SsqConstant;
import win.caicaikan.constant.SysConfig;
import win.caicaikan.repository.mongodb.entity.SysConfigEntity;
import win.caicaikan.repository.mongodb.entity.ssq.SsqResultEntity;
import win.caicaikan.service.internal.DaoService;
import win.caicaikan.util.DateUtil;

@Service
public class RuleService {

	@Autowired
	private DaoService daoService;

	public String getCurrentTermNoOfSsq() {
		SysConfigEntity currentTerm = daoService.queryById(SysConfig.SSQ_CURRENT_TERM.getId(),
				SysConfigEntity.class);
		Date openDate;
		try {
			openDate = DateUtil._SECOND.parse(currentTerm.getKey());
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
		String termNo = currentTerm.getValue();
		while (openDate.compareTo(new Date()) < 0) {
			int yearOfThisTerm = DateUtil.getYear(openDate);
			int week = DateUtil.getWeek(openDate);

			int daysToNextTerm = 2;
			// 周四到下期要三天
			if (week == Calendar.THURSDAY) {
				daysToNextTerm++;
			}
			Date addDays = DateUtil.addDays(openDate, daysToNextTerm);
			if (addDays.compareTo(new Date()) > 0) {
				break;
			}
			openDate = addDays;
			int yearOfNextTerm = DateUtil.getYear(openDate);
			if (yearOfNextTerm != yearOfThisTerm) {
				termNo = yearOfNextTerm + SsqConstant.START_NO;
			} else {
				termNo = String.valueOf(Integer.valueOf(termNo) + 1);
			}
		}
		currentTerm.setKey(DateUtil._SECOND.format(openDate));
		currentTerm.setValue(termNo);
		daoService.save(currentTerm);
		return termNo;
	}

	public String getNextTermNoOfSsq() {
		SysConfigEntity nextTerm = daoService.queryById(SysConfig.SSQ_NEXT_TERM.getId(),
				SysConfigEntity.class);
		Date openDate;
		try {
			openDate = DateUtil._SECOND.parse(nextTerm.getKey());
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
		String termNo = nextTerm.getValue();
		while (openDate.compareTo(new Date()) < 0) {
			int yearOfThisTerm = DateUtil.getYear(openDate);
			int week = DateUtil.getWeek(openDate);

			int daysToNextTerm = 2;
			// 周四到下期要三天
			if (week == Calendar.THURSDAY) {
				daysToNextTerm++;
			}
			openDate = DateUtil.addDays(openDate, daysToNextTerm);
			int yearOfNextTerm = DateUtil.getYear(openDate);

			if (yearOfNextTerm != yearOfThisTerm) {
				termNo = yearOfNextTerm + SsqConstant.START_NO;
			} else {
				termNo = String.valueOf(Integer.valueOf(termNo) + 1);
			}
		}
		nextTerm.setKey(DateUtil._SECOND.format(openDate));
		nextTerm.setValue(termNo);
		daoService.save(nextTerm);
		return termNo;
	}

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
			return yearOfNextTerm + SsqConstant.START_NO;
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

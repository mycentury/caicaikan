package win.caicaikan.service.rule;

import java.math.BigInteger;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import win.caicaikan.constant.SsqConstant;
import win.caicaikan.constant.SysConfig;
import win.caicaikan.repository.mongodb.entity.SysConfigEntity;
import win.caicaikan.repository.mongodb.entity.ssq.SsqResultEntity;
import win.caicaikan.service.internal.DaoService;
import win.caicaikan.util.DateUtil;
import win.caicaikan.util.MathUtil;

/**
 * @desc 通用转换计算规则服务
 * @author yanwenge
 * @date 2017年1月8日
 * @class RuleService
 */
@Service
public class RuleService {
	private static final Logger logger = Logger.getLogger(RuleService.class);
	private static final BigInteger RED_MAX = new BigInteger("33");
	private static final BigInteger BLUE_MAX = new BigInteger("16");
	private static final BigInteger RED_VALID = new BigInteger("6");
	private static final BigInteger BLUE_VALID = BigInteger.ONE;
	private static final BigInteger RED_INVALID = new BigInteger("27");
	private static final BigInteger BLUE_INVALID = new BigInteger("15");
	private static final BigInteger RED_COUNT = new BigInteger("10");
	private static final BigInteger BLUE_COUNT = new BigInteger("3");
	@Autowired
	private DaoService daoService;

	public BigInteger countSsqAllCases() {
		BigInteger red = MathUtil.C(RED_MAX, RED_VALID);
		BigInteger blue = MathUtil.C(BLUE_MAX, BLUE_VALID);
		BigInteger allCases = red.multiply(blue);
		return allCases;
	}

	public BigInteger countSsqPredictCases() {
		BigInteger red = MathUtil.C(RED_COUNT, RED_VALID);
		BigInteger blue = MathUtil.C(BLUE_COUNT, BLUE_VALID);
		BigInteger predictCases = red.multiply(blue);
		return predictCases;
	}

	public BigInteger countSsqPrizeCases(String prizecondition) {
		if (StringUtils.isEmpty(prizecondition)) {
			throw new RuntimeException("参数有误：" + prizecondition);
		}
		String[] split = prizecondition.split(";");
		BigInteger prizeCases = BigInteger.ZERO;
		for (String string : split) {
			String[] split2 = string.split("\\+");
			if (split2.length != 2) {
				throw new RuntimeException("参数有误：" + prizecondition);
			}
			BigInteger redNeeded = new BigInteger(split2[0]);
			BigInteger blueNeeded = new BigInteger(split2[1]);
			if (redNeeded.compareTo(BigInteger.ZERO) < 0 || redNeeded.compareTo(RED_VALID) > 0 || blueNeeded.compareTo(BigInteger.ZERO) < 0
					|| blueNeeded.compareTo(BLUE_VALID) > 0) {
				throw new RuntimeException("参数有误：" + prizecondition);
			}
			BigInteger red = MathUtil.C(RED_VALID, redNeeded);
			red = red.multiply(MathUtil.C(RED_INVALID, RED_VALID.subtract(redNeeded)));
			BigInteger blue = MathUtil.C(BLUE_VALID, blueNeeded);
			blue = blue.multiply(MathUtil.C(BLUE_INVALID, BLUE_VALID.subtract(blueNeeded)));
			prizeCases = prizeCases.add(red.multiply(blue));
		}
		return prizeCases;
	}

	public String getCurrentTermNoOfSsq() {
		SysConfigEntity currentTerm = daoService.queryById(SysConfig.SSQ_CURRENT_TERM.getId(), SysConfigEntity.class);
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
		SysConfigEntity nextTerm = daoService.queryById(SysConfig.SSQ_NEXT_TERM.getId(), SysConfigEntity.class);
		Date openDate;
		try {
			openDate = DateUtil._SECOND.parse(nextTerm.getKey());
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
		String termNo = nextTerm.getValue();
		boolean changed = false;
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
			logger.info(termNo + "=" + DateUtil._SECOND.format(openDate));
			changed = true;
		}
		if (changed) {
			nextTerm.setKey(DateUtil._SECOND.format(openDate));
			nextTerm.setValue(termNo);
			daoService.save(nextTerm);
		}
		return termNo;
	}

	public String getNextTermNoOfSsq(SsqResultEntity entity) {
		Date thisDate = null;
		try {
			thisDate = DateUtil._SECOND.parse(entity.getOpenTime());
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
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
			return String.valueOf(Integer.valueOf(entity.getId()) + 1);
		}
	}

	public String getNextTermOpenDateOfSsq(String openTime) {
		Date thisDate = null;
		try {
			thisDate = DateUtil._SECOND.parse(openTime);
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
		int week = DateUtil.getWeek(thisDate);

		int daysToNextTerm = 2;
		// 周四到下期要三天
		if (week == Calendar.THURSDAY) {
			daysToNextTerm++;
		}
		Date nextDate = DateUtil.addDays(thisDate, daysToNextTerm);
		return DateUtil._SECOND.format(nextDate);
	}
}

/**
 * 
 */
package win.caicaikan.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @Desc
 * @author wewenge.yan
 * @Date 2016年11月25日
 * @ClassName DatePattern
 */
public class DateUtil {
	/**
	 * 格式:年
	 */
	public static final SimpleDateFormat YEAR = new SimpleDateFormat("yyyy");
	/**
	 * 格式:年月日
	 */
	public static final SimpleDateFormat DAY = new SimpleDateFormat("yyyyMMdd");
	/**
	 * 格式:年-月-日
	 */
	public static final SimpleDateFormat _DAY = new SimpleDateFormat("yyyy-MM-dd");
	/**
	 * 格式:年/月/日
	 */
	public static final SimpleDateFormat DAY_ = new SimpleDateFormat("yyyy/MM/dd");
	/**
	 * 格式:年-月-日 时:分:秒
	 */
	public static final SimpleDateFormat _SECOND = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	public static String toYear(Date date) {
		return YEAR.format(date);
	}

	public static String toDate(Date date) {
		return DAY.format(date);
	}

	public static Date addDays(Date date, int days) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.DAY_OF_YEAR, days);
		return calendar.getTime();
	}

	public static int getYear(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar.get(Calendar.YEAR);
	}

	public static int getMonth(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar.get(Calendar.MONTH);
	}

	public static int getDay(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar.get(Calendar.DAY_OF_MONTH);
	}

	public static int getWeek(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar.get(Calendar.DAY_OF_WEEK);
	}

	public static int getHour(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar.get(Calendar.HOUR_OF_DAY);
	}
}

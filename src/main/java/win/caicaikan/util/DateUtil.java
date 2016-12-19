/**
 * 
 */
package win.caicaikan.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

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

	/**
	 * ISO8601_DATE_FORMAT
	 */
	public static final String ISO8601 = "yyyy-MM-dd'T'HH:mm:ss'Z'";

	public static String toYear(Date date) {
		return YEAR.format(date);
	}

	public static Date toDate(String source, String format) {
		try {
			return new SimpleDateFormat(format).parse(source);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String toChar(Date date, String format) {
		return new SimpleDateFormat(format).format(date);
	}

	public static String toChar(Date date) {
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

	public static Date getTimeByTimeZone(String timeZone) {
		TimeZone zone = TimeZone.getTimeZone(timeZone);
		return Calendar.getInstance(zone).getTime();
	}

	public static Date getUtcTime() {
		// 1、取得本地时间：
		Calendar cal = Calendar.getInstance();
		// 2、取得时间偏移量：
		int zoneOffset = cal.get(Calendar.ZONE_OFFSET);
		// 3、取得夏令时差：
		int dstOffset = cal.get(Calendar.DST_OFFSET);
		// 4、从本地时间里扣除这些差量，即可以取得UTC时间：
		cal.add(Calendar.MILLISECOND, -(zoneOffset + dstOffset));
		return cal.getTime();
	}

}

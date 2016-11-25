/**
 * 
 */
package win.caicaikan.util;

import java.text.SimpleDateFormat;
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
	public static final SimpleDateFormat DATE = new SimpleDateFormat("yyyyMMdd");
	/**
	 * 格式:年-月-日
	 */
	public static final SimpleDateFormat _DATE = new SimpleDateFormat("yyyy-MM-dd");
	/**
	 * 格式:年/月/日
	 */
	public static final SimpleDateFormat DATE_ = new SimpleDateFormat("yyyy/MM/dd");

	public static String toYear(Date date) {
		return YEAR.format(date);
	}

	public String toDate(Date date) {
		return DATE.format(date);
	}
}

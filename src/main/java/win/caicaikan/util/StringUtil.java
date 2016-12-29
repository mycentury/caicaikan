/**
 * 
 */
package win.caicaikan.util;

/**
 * @Desc
 * @author wewenge.yan
 * @Date 2016年12月29日
 * @ClassName StringUtil
 */
public class StringUtil {

	public static String initialToUpper(String source) {
		if (source == null || source.length() <= 0) {
			return source;
		}
		char[] charArray = source.toCharArray();
		if ('a' <= charArray[0] && charArray[0] <= 'z') {
			charArray[0] -= 'a' - 'A';
		}
		return new String(charArray);
	}

	public static String initialToLower(String source) {
		if (source == null || source.length() <= 0) {
			return source;
		}
		char[] charArray = source.toCharArray();
		if ('A' <= charArray[0] && charArray[0] <= 'Z') {
			charArray[0] += 'a' - 'A';
		}
		return new String(charArray);
	}
}

/**
 * 
 */
package win.caicaikan.util;

import java.lang.reflect.Type;
import java.util.Map;

import com.google.gson.Gson;

/**
 * @Desc
 * @author wewenge.yan
 * @Date 2016年12月26日
 * @ClassName GsonUtil
 */
public class GsonUtil {
	private final static Gson gson = new Gson();

	public static <T> T fromJson(String json, Class<T> classOfT) {
		return gson.fromJson(json, classOfT);
	}

	public static <T> T fromJson(String json, Type typeOfT) {
		// Type jsonType = new TypeToken<Map<String, Integer>>() {}.getType();
		return gson.fromJson(json, typeOfT);
	}

	public static String toJson(Object obj) {
		return gson.toJson(obj);
	}
}

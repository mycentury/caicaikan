package win.caicaikan.util;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class TypeConverterUtil {
	private TypeConverterUtil() {
	}

	public static <T> T map(final Object source, final Class<T> destClazz) {
		final Map<String, Object> map = changeSourceToMap(source);
		return changeMapToDest(destClazz, map);
	}

	@SuppressWarnings("unchecked")
	private static <T> T changeMapToDest(final Class<T> destClazz, final Map<String, Object> map) {
		if (Map.class.equals(destClazz)) {
			return (T) map;
		}
		T dest = null;
		try {
			dest = destClazz.newInstance();
		} catch (final InstantiationException e) {
			e.printStackTrace();
			return null;
		} catch (final IllegalAccessException e) {
			e.printStackTrace();
			return null;
		}
		Class<?> clazz = destClazz;
		while (!clazz.equals(Object.class)) {
			Field fields[] = clazz.getDeclaredFields();
			for (Field field : fields) {
				//final static等修饰符过滤
				if (field.getModifiers() > 4) {
					continue;
				}
				final boolean access = field.isAccessible();
				try {
					field.setAccessible(true);
					final Object value = map.get(field.getName());
					if (value != null && field.getType().equals(value.getClass())) {
						field.set(dest, value);
					}
					field.setAccessible(access);
				} catch (final IllegalArgumentException e) {
					e.printStackTrace();
				} catch (final IllegalAccessException e) {
					e.printStackTrace();
				}
			}
			clazz = clazz.getSuperclass();
		}
		return dest;
	}

	@SuppressWarnings("rawtypes")
	public static Map<String, Object> changeSourceToMap(final Object source) {
		final Map<String, Object> map = new HashMap<String, Object>();
		if (source == null) {
			return map;
		}
		if (source instanceof Map) {
			Map map2 = (Map) source;
			for (Object key : map2.keySet()) {
				if (key == null) {
					continue;
				}
				map.put(key.toString(), map2.get(key));
			}
			return map;
		}
		Class<? extends Object> sourceClazz = source.getClass();
		while (!sourceClazz.equals(Object.class)) {
			final Field[] fields = sourceClazz.getDeclaredFields();
			for (final Field field : fields) {
				try {
					final boolean access = field.isAccessible();
					field.setAccessible(true);
					final Object object = field.get(source);
					field.setAccessible(access);
					map.put(field.getName(), object);
				} catch (final IllegalArgumentException | IllegalAccessException e) {
					throw new RuntimeException(e);
				}
			}
			sourceClazz = sourceClazz.getSuperclass();
		}

		return map;
	}

	public static String convertColToCamel(String source) {
		if (source == null) {
			throw new RuntimeException("source can't be null!");
		}
		char[] charArray = source.toLowerCase().toCharArray();
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < charArray.length; i++) {
			if (charArray[i] == '_') {
				if (i + 1 < charArray.length) {
					sb.append(String.valueOf(charArray[i + 1]).toUpperCase());
					i++;
				}
			} else {
				sb.append(charArray[i]);
			}
		}
		return sb.toString();
	}

	public static String convertCamelToCol(String source) {
		if (source == null) {
			throw new RuntimeException("source can't be null!");
		}
		char[] charArray = source.toCharArray();
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < charArray.length; i++) {
			if (charArray[i] > 'A' && charArray[i] < 'Z') {
				sb.append("_");
			}
			sb.append(charArray[i]);
		}
		return sb.toString().toUpperCase();
	}

	public static void main(String[] args) {
		System.out.println(convertCamelToCol("convertCamelToCol"));
		System.out.println(convertColToCamel("CONVERT_CAMEL_TO_COL"));
	}
}

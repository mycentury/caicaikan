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

	@SuppressWarnings("unchecked")
	private static Map<String, Object> changeSourceToMap(final Object source) {
		Map<String, Object> map = new HashMap<String, Object>();
		if (source instanceof Map) {
			map = (HashMap<String, Object>) source;
		} else {
			final Class<? extends Object> sourceClazz = source.getClass();
			final Field[] fields = sourceClazz.getDeclaredFields();
			for (final Field field : fields) {
				final boolean access = field.isAccessible();
				try {
					field.setAccessible(true);
					final Object object = field.get(source);
					field.setAccessible(access);
					map.put(field.getName(), object);
				} catch (final IllegalArgumentException e) {
					e.printStackTrace();
				} catch (final IllegalAccessException e) {
					e.printStackTrace();
				}
			}
		}
		return map;
	}

	public static Map<String, Object> changeSourceToColMap(final Object source) {
		final Map<String, Object> map = new HashMap<String, Object>();
		Class<? extends Object> sourceClazz = source.getClass();
		while (!sourceClazz.equals(Object.class)) {
			final Field[] fields = sourceClazz.getDeclaredFields();
			for (final Field field : fields) {
				final boolean access = field.isAccessible();
				try {
					field.setAccessible(true);
					final Object object = field.get(source);
					field.setAccessible(access);
					map.put(field.getName(), object);
				} catch (final IllegalArgumentException e) {
					e.printStackTrace();
				} catch (final IllegalAccessException e) {
					e.printStackTrace();
				}
			}
			sourceClazz = sourceClazz.getSuperclass();
		}

		return map;
	}
}
package win.caicaikan.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class MapUtil {
	public static final String ASC = "ASC";
	public static final String DESC = "DESC";

	public static <K, V extends Comparable<V>> Map<K, V> sortToMapByValue(Map<K, V> map, String sort) {
		Comparator<Map.Entry<K, V>> comparator = new Comparator<Map.Entry<K, V>>() {
			@Override
			public int compare(Entry<K, V> o1, Entry<K, V> o2) {
				if (DESC.equals(sort)) {
					return o2.getValue().compareTo(o1.getValue());
				}
				return o1.getValue().compareTo(o2.getValue());
			}

		};
		List<Map.Entry<K, V>> list = new LinkedList<Map.Entry<K, V>>(map.entrySet());
		Collections.sort(list, comparator);
		Map<K, V> result = new LinkedHashMap<K, V>();
		for (Map.Entry<K, V> entry : list) {
			result.put(entry.getKey(), entry.getValue());
		}
		return result;
	}

	public static <K, V extends Comparable<V>> List<Map.Entry<K, V>> sortToListByValue(
			Map<K, V> map, String sort) {
		Comparator<Map.Entry<K, V>> comparator = new Comparator<Map.Entry<K, V>>() {
			@Override
			public int compare(Entry<K, V> o1, Entry<K, V> o2) {
				if (DESC.equals(sort)) {
					return o2.getValue().compareTo(o1.getValue());
				}
				return o1.getValue().compareTo(o2.getValue());
			}

		};
		List<Map.Entry<K, V>> list = new ArrayList<Map.Entry<K, V>>(map.entrySet());
		Collections.sort(list, comparator);
		return list;
	}
}

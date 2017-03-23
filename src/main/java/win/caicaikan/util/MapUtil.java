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

	public static <K, V extends Comparable<V>> Map<K, V> sortMapToMapByValue(Map<K, V> map, String sort) {
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

	public static <K extends Comparable<K>, V extends Comparable<V>> List<Map.Entry<K, V>> sortMapToEntryListBykey(Map<K, V> map, String sort) {
		Comparator<Map.Entry<K, V>> comparator = new Comparator<Map.Entry<K, V>>() {
			@Override
			public int compare(Entry<K, V> o1, Entry<K, V> o2) {
				if (DESC.equals(sort)) {
					int compareTo = o2.getKey().compareTo(o1.getKey());
					return compareTo == 0 ? o1.getValue().compareTo(o2.getValue()) : compareTo;
				}
				int compareTo = o1.getKey().compareTo(o2.getKey());
				return compareTo == 0 ? o1.getValue().compareTo(o2.getValue()) : compareTo;
			}

		};
		List<Map.Entry<K, V>> list = new ArrayList<Map.Entry<K, V>>(map.entrySet());
		Collections.sort(list, comparator);
		return list;
	}

	public static <K extends Comparable<K>, V extends Comparable<V>> List<Map.Entry<K, V>> sortMapToEntryListByValue(Map<K, V> map, String sort) {
		Comparator<Map.Entry<K, V>> comparator = new Comparator<Map.Entry<K, V>>() {
			@Override
			public int compare(Entry<K, V> o1, Entry<K, V> o2) {
				if (DESC.equals(sort)) {
					int compareTo = o2.getValue().compareTo(o1.getValue());
					return compareTo == 0 ? o1.getKey().compareTo(o2.getKey()) : compareTo;
				}
				int compareTo = o1.getValue().compareTo(o2.getValue());
				return compareTo == 0 ? o1.getKey().compareTo(o2.getKey()) : compareTo;
			}

		};
		List<Map.Entry<K, V>> list = new ArrayList<Map.Entry<K, V>>(map.entrySet());
		Collections.sort(list, comparator);
		return list;
	}

	/**
	 * @param redMap
	 * @return
	 */
	public static <K extends Comparable<K>, V extends Comparable<V>> List<String> sortMapToListByValue(Map<K, V> map, String splitor, String sort) {
		List<String> result = new ArrayList<String>();
		List<Entry<K, V>> entries = sortMapToEntryListByValue(map, DESC.equals(sort) ? DESC : ASC);
		for (Entry<K, V> entry : entries) {
			result.add(entry.getKey() + splitor + entry.getValue());
		}
		return result;
	}

	/**
	 * @param redMap
	 * @return
	 */
	public static <K extends Comparable<K>, V extends Comparable<V>> List<String> sortMapToListByKey(Map<K, V> map, String splitor, String sort) {
		List<String> result = new ArrayList<String>();
		List<Entry<K, V>> entries = sortMapToEntryListBykey(map, DESC.equals(sort) ? DESC : ASC);
		for (Entry<K, V> entry : entries) {
			result.add(entry.getKey() + splitor + entry.getValue());
		}
		return result;
	}

	public static <K, V> void initMapKeysWithValue(Map<K, V> map, K[] keys, V value) {
		for (K key : keys) {
			map.put(key, value);
		}
	}
}

package com.ai3cloud.util;

import static java.util.Collections.reverseOrder;
import static java.util.Map.Entry.comparingByValue;
import static java.util.function.Function.identity;
import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toMap;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * A utility class providing generical convenience methods.
 * 
 * @author Jiayun Han
 * @since 1.1.0.0
 */
public class Tool {

	/**
	 * Returns the most frequent element of the list
	 * 
	 * @param list
	 * @return The most frequent element of the list
	 */
	public static <T> T getMostFrequent(List<T> list) {

		Map<T, Long> map = list.stream().collect(groupingBy(identity(), counting()));
		map = sortByValue(map, SortOrder.DESCENDING);
		return getFirstKey(map);

	}

	/**
	 * Sort a map by its value and returns the sorted map
	 * 
	 * @param <K>
	 *            The element type of the map's key
	 * @param <V>
	 *            The element type of the map's value
	 * @param map
	 *            the map to be sorted based on its value
	 * @param sortOrder
	 *            whether it is descending or ascending
	 * @return A new map sorted on the old map's values
	 */
	public static <K, V extends Comparable<? super V>> Map<K, V> sortByValue(Map<K, V> map, SortOrder sortOrder) {

		Comparator<? super Entry<K, V>> comparator = //
				sortOrder == SortOrder.DESCENDING ? //
						reverseOrder(comparingByValue()) : //
						comparingByValue();

		return map.entrySet() //
				.stream() //
				.sorted(comparator)//
				.collect(toMap(Entry::getKey, Entry::getValue, (e1, e2) -> e2, LinkedHashMap::new));
	}

	/**
	 * Returns the key of the first entry of the map
	 * 
	 * @param map
	 *            whose first entry's key to be returned
	 * @return The key of the first entry of the map
	 */
	public static <K> K getFirstKey(Map<K, ?> map) {
		return map.keySet().iterator().next();
	}

	/**
	 * Make a copy of the list without copying the element at index
	 * 
	 * @param list
	 *            that to be conditionally copied
	 * @param index
	 *            The element at this index will be skipped
	 * @return A copy of the list without copying the element at index
	 */
	public static <T> List<T> copyAndSkip(List<T> list, int index) {

		List<T> list2 = new ArrayList<>(list.size() - 1);
		for (int i = 0; i < list.size(); i++) {
			if (i != index) {
				list2.add(list.get(i));
			}
		}

		return list2;
	}
}

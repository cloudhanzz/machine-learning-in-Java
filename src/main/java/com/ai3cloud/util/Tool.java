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

public class Tool {

	public static <T> T getMostFrequent(List<T> list) {

		Map<T, Long> map = list.stream().collect(groupingBy(identity(), counting()));
		map = sortByValue(map, SortOrder.DESCENDING);
		return getFirstKey(map);

	}

	public static <T> Map<T, Long> sortByValue(Map<T, Long> map, SortOrder sortOrder) {

		Comparator<? super Entry<T, Long>> comparator = //
				sortOrder == SortOrder.DESCENDING ? //
						reverseOrder(comparingByValue()) : //
						comparingByValue();

		return map.entrySet() //
				.stream() //
				.sorted(comparator)//
				.collect(toMap(Entry::getKey, Entry::getValue, (e1, e2) -> e2, LinkedHashMap::new));
	}

	public static <T> T getFirstKey(Map<T, Long> map) {
		return map.keySet().iterator().next();
	}
	
	/**
	 * Make a copy of the list without the element at index
	 * @param list
	 * @param index
	 * @return
	 */
	public static <T> List<T> copyAndSkip(List<T> list, int index){
		
		List<T> list2 = new ArrayList<>(list.size() - 1);
		for(int i = 0; i < list.size(); i++) {
			if(i != index) {
				list2.add(list.get(i));
			}
		}
		
		return list2;
	}
}

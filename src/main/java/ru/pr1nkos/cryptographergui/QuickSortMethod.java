package ru.pr1nkos.cryptographergui;

import java.util.*;

public class QuickSortMethod {

	public static Map<String, Integer> sortDictionary(Map<String, Integer> dictionary) {
		List<Map.Entry<String, Integer>> entries = new ArrayList<>(dictionary.entrySet());

		quickSort(entries, 0, entries.size() - 1);

		Map<String, Integer> sortedDictionary = new LinkedHashMap<>();
		for (Map.Entry<String, Integer> entry : entries) {
			sortedDictionary.put(entry.getKey(), entry.getValue());
		}

		return sortedDictionary;
	}

	private static void quickSort(List<Map.Entry<String, Integer>> entries, int low, int high) {
		if (low < high) {
			int pivotIndex = partition(entries, low, high);
			quickSort(entries, low, pivotIndex - 1);
			quickSort(entries, pivotIndex + 1, high);
		}
	}

	private static int partition(List<Map.Entry<String, Integer>> entries, int low, int high) {
		int pivot = entries.get(high).getValue();
		int i = low - 1;

		for (int j = low; j < high; j++) {
			if (entries.get(j).getValue() >= pivot) {
				i++;
				Collections.swap(entries, i, j);
			}
		}

		Collections.swap(entries, i + 1, high);
		return i + 1;
	}
}
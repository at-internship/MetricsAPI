package com.metrics.service;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.metrics.model.MetricsCollection;

public class SortingValidations {
	public static List<MetricsCollection> OrderByAscending(List<MetricsCollection> listMetric) {
		List<MetricsCollection> listOrder = listMetric;
		Collections.sort(listOrder, new Comparator<MetricsCollection>() {
			@Override
			public int compare(MetricsCollection o1, MetricsCollection o2) {
				return o1.getDate().compareTo(o2.getDate());
			}

		});
		return listOrder;
	}

	public static List<MetricsCollection> OrderByDescending(List<MetricsCollection> listMetric) {
		List<MetricsCollection> listOrder = listMetric;
		Collections.sort(listOrder, new Comparator<MetricsCollection>() {
			@Override
			public int compare(MetricsCollection o1, MetricsCollection o2) {

				return o2.getDate().compareTo(o1.getDate());

			}

		});
		return listOrder;
	}
}

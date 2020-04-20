package com.metrics.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import com.metrics.domain.CreateMetricRequest;
import com.metrics.model.MetricsCollection;

public interface MetricsService {

	List<MetricsCollection> getMetrics();

	Optional<MetricsCollection> findById(String id);

	MetricsCollection newMetric(CreateMetricRequest request);

	void deleteMetric(String id);

	MetricsCollection updateMetric(CreateMetricRequest request, String id);

	List<MetricsCollection> getAllMetricsPaginated(int start, int size, List<MetricsCollection> metrics);

	public List<MetricsCollection> getItemsFromIdFilter(String id, List<MetricsCollection> metrics, int typeId,
			int typeRequest);

	List<MetricsCollection> getItemsFromDateRange(Date startDate, Date endDate, List<MetricsCollection> metrics);
}
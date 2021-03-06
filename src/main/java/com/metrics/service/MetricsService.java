package com.metrics.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import com.metrics.domain.CreateMetricRequest;
import com.metrics.model.MetricsCollection;

public interface MetricsService {

	MetricsCollection findById(String id);

	MetricsCollection newMetric(CreateMetricRequest request);

	void deleteMetric(String id);

	MetricsCollection updateMetric(CreateMetricRequest request, String id);
	
	List<MetricsCollection> getMetricsFilter(String sprint_id, String evaluator_id,
			String evaluated_id, String startDate,
			String endDate, int page, int size, int order);
}
package com.metrics.service;

import java.util.List;
import java.util.Optional;
import com.metrics.domain.CreateMetricRequest;
import com.metrics.model.MetricsCollection;

public interface MetricsService {

	List<MetricsCollection> getMetrics();
	Optional <MetricsCollection> findById(String id);
    MetricsCollection newMetric(CreateMetricRequest request);
	void deleteMetric(String id);
    MetricsCollection updateMetric(CreateMetricRequest request, String id);
}
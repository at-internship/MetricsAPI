package com.metrics.service;

import java.util.List;

import com.metrics.model.MetricsCollection;


public interface MetricsService {

	List<MetricsCollection> getMetrics();
}

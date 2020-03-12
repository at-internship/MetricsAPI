package com.metrics.service;
import com.metrics.domain.CreateMetricRequest;
import com.metrics.model.MetricsCollection;
public interface MetricsService 
{	
	MetricsCollection newMetric(CreateMetricRequest request);
	
}

package com.metrics.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.bson.types.ObjectId;

import com.metrics.domain.CreateMetricRequest;
import com.metrics.model.MetricsCollection;

public interface MetricsService {

	List<MetricsCollection> getMetrics();
	
	Optional <MetricsCollection> findById(String id);
	
 	MetricsCollection newMetric(CreateMetricRequest request);
  
	void deleteMetric(String id);
	
	MetricsCollection updateMetric(CreateMetricRequest request, String id);
	
	public List<MetricsCollection> getAllMetricsPaginated(int start, int size, List<MetricsCollection> metrics, int orderBy);
	
	public List<MetricsCollection> getItemsFromDateRange(Date startDate, Date endDate, List<MetricsCollection> metrics, int orderBy);
	
	public List<MetricsCollection> getSpecificItemsFromTypeIdRange(String startId, String endId, List<MetricsCollection> metrics, int filtredBy, int orderBy);
}
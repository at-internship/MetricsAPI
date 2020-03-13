package com.metrics.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.metrics.domain.CreateMetricRequest;
import com.metrics.model.MetricsCollection;
import com.metrics.repository.MetricRepository;
import ma.glasnost.orika.MapperFacade;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@JsonIgnoreProperties(ignoreUnknown = true)
public class MetricsServiceImpl implements MetricsService
{
	@Autowired
	private MapperFacade orikaMapperFacade;
	@Autowired
	MetricRepository repository;
  
	@Override
	public MetricsCollection newMetric(CreateMetricRequest request)
	{
			MetricsCollection metric = new MetricsCollection();
			metric = orikaMapperFacade.map(request, MetricsCollection.class);
			repository.save(metric);
			return metric;
	}
	
	 public void deleteMetric(String id) {
		 if (repository.existsById(id)) {
			 repository.deleteById(id);
		 }
		 else {
			 throw new ResponseStatusException(HttpStatus.NOT_FOUND, "The given ID does not exist");
		 }
	 }
}

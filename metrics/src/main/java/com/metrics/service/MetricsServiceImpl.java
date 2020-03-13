package com.metrics.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.server.ResponseStatusException;

import com.metrics.domain.CreateMetricRequest;
import com.metrics.model.MetricsCollection;
import com.metrics.repository.MetricRepository;

import ma.glasnost.orika.MapperFacade;

@Service
public class MetricsServiceImpl implements MetricsService {
	@Autowired
	MetricRepository repository;

	@Autowired
	private MapperFacade orikaMapperFacade;
	
	@Override
	public MetricsCollection updateMetric(CreateMetricRequest request, String id) {
		if (!repository.existsById(id))
			throw new ResponseStatusException(
			          HttpStatus.NOT_FOUND, "Metric not found");
		MetricsCollection metric = new MetricsCollection();
		MappingTest test = new MappingTest();
		if(test.MappingTestMetric(request)) {
			
			metric = orikaMapperFacade.map(request, MetricsCollection.class);
			metric.setId(id);
		}else {
			throw new ResponseStatusException(
			          HttpStatus.BAD_REQUEST);
		}
		
		return repository.save(metric);
	}
	
	
}

package com.metrics.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

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
			throw new HttpClientErrorException(HttpStatus.NOT_FOUND, "No user found with the given id");
		MetricsCollection metric = new MetricsCollection();
		metric = orikaMapperFacade.map(request, MetricsCollection.class);
		metric.setId(id);
		return repository.save(metric);
	}
}

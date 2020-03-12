package com.metrics.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.metrics.domain.CreateMetricRequest;
import com.metrics.model.MetricsCollection;
import com.metrics.repository.MetricRepository;
import ma.glasnost.orika.MapperFacade;

@Service
@JsonIgnoreProperties(ignoreUnknown = true)
public class MetricsServiceImpl implements MetricsService
{

	
	//private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(MetricRepository.class);
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
	
}

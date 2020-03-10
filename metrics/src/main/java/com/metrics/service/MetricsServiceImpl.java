package com.metrics.service;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.metrics.model.MetricsCollection;
import com.metrics.repository.MetricRepository;

@Service
public class MetricsServiceImpl implements MetricsService{
	private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(MetricsServiceImpl.class);

	@Autowired
	MetricRepository repository;
	
	@Override
	public List<MetricsCollection> getMetrics() {
		List<MetricsCollection> metricsCollection = new ArrayList<>();
		
		//log.warn("No user was found for given phone & first name combination.");
		metricsCollection = repository.findAll();
		log.debug("Records found on DB - " + metricsCollection.toString());
		return metricsCollection;
	}

}

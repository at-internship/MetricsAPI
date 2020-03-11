package com.metrics.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

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
		metricsCollection = repository.findAll();
		log.debug("Records found on DB - " + metricsCollection.toString());
		return metricsCollection;
	}
	
	@Override
	public Optional<MetricsCollection> findById(String id) {
		if (!repository.existsById(id))
			throw new HttpClientErrorException(HttpStatus.NOT_FOUND, "No metric found with the given id");	
		return repository.findById(id);
    }

}

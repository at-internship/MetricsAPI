package com.metrics.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.metrics.repository.MetricRepository;

@Service
public class MetricsServiceImpl {
	private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(MetricsServiceImpl.class);
	
	@Autowired
	MetricRepository repository;

	 public void deleteMetric(String id) {
		 if (repository.existsById(id)) {
			 repository.deleteById(id);
		 }
		 else {
			 throw new ResponseStatusException(HttpStatus.NOT_FOUND, "The given ID does not exist");
		 }
	 }
}

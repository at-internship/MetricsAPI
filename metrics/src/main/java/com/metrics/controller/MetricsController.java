package com.metrics.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.metrics.domain.CreateMetricRequest;
import com.metrics.model.MetricsCollection;
import com.metrics.service.MappingTest;
import com.metrics.service.MetricsServiceImpl;

@RestController
public class MetricsController {
	private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(MetricsController.class);
	@Autowired
	MetricsServiceImpl service;
	
	@ResponseStatus(value = HttpStatus.ACCEPTED	)
	@PutMapping("/metrics/{id}")
	public MetricsCollection updateMetric(@RequestBody CreateMetricRequest request, @PathVariable String id) {
		MetricsCollection resultMetric = new MetricsCollection();
		log.debug("Update user request - id=" + id + " " + request.toString());
		
			
		resultMetric = service.updateMetric(request, id);
		
		return resultMetric;
	}	
	
}

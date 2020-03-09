package com.metrics.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.metrics.model.MetricsCollection;
import com.metrics.service.MetricsServiceImpl;

@RestController
public class MetricsController {
	private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(MetricsController.class);

	
	@Autowired
	MetricsServiceImpl service;
	
	
	@ResponseStatus(value = HttpStatus.OK)
	@GetMapping("/metric")
	public List<MetricsCollection> getMetrics() {
		log.debug("Records test:");
		return service.getMetrics();
	}

}

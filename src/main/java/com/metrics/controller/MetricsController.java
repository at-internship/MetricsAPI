package com.metrics.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.metrics.MetricsApplication;
import com.metrics.domain.CreateMetricRequest;
import com.metrics.model.MetricsCollection;
import com.metrics.service.MappingTest;
import com.metrics.service.MetricsServiceImpl;
import java.util.List;
import java.util.Optional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.DeleteMapping;

@RestController
public class MetricsController {
	@Autowired
	MetricsServiceImpl service;

	@ResponseStatus(value = HttpStatus.OK)
	@PutMapping("/metrics/{id}")
	public MetricsCollection updateMetric(@RequestBody CreateMetricRequest request, @PathVariable String id) {
		MetricsApplication.logger.info("Creating container");
		MetricsCollection resultMetric = new MetricsCollection();
		
		MetricsApplication.logger.info("calling update service");
		resultMetric = service.updateMetric(request, id);
		MetricsApplication.logger.info("update successfull, returning updated object..");
		return resultMetric;
	}

	@ResponseStatus(value = HttpStatus.OK)
	@GetMapping("/metrics")
	public List<MetricsCollection> getMetrics() {
		MetricsApplication.logger.info("Calling getMetrics service");
		return service.getMetrics();
	}

	@ResponseStatus(value = HttpStatus.OK)
	@GetMapping("/metrics/{id}")
	public Optional<MetricsCollection> findById(@PathVariable String id) {
		try {
			MetricsApplication.logger.info("Calling findById service");
			return service.findById(id);
		} catch (Exception error) {
			MetricsApplication.logger.error("trying to call findById service but couldnt find the given ID");
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Metric not found", error);
		}
	}

	@ResponseStatus(value = HttpStatus.CREATED)
	@PostMapping("/metrics")
	public String newMetric(@RequestBody CreateMetricRequest request) {
		String id = "";
		MetricsApplication.logger.info("Calling the data validation method");
		MappingTest test = new MappingTest();
		if (test.MappingTestMetric(request)) {
			MetricsApplication.logger.info("data validation successfull,calling the newMetric service");
			id = service.newMetric(request).getId();
			MetricsApplication.logger.info("saving id into String to return");
		}
		MetricsApplication.logger.info("New metric created successfully returning the ID of the new metric");
		return id;
	}

	@ResponseStatus(value = HttpStatus.NO_CONTENT)
	@DeleteMapping("/metrics/{id}")
	public void deleteMetric(@PathVariable String id) {
		MetricsApplication.logger.info("Calling deleteMetric service");
		service.deleteMetric(id);
	}

}

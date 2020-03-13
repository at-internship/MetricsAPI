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
import com.metrics.domain.CreateMetricRequest;
import com.metrics.model.MetricsCollection;
import com.metrics.service.MappingTest;
import com.metrics.service.MetricsServiceImpl;
import java.util.List;
import java.util.Optional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
  
  	@ResponseStatus(value = HttpStatus.OK)
	@GetMapping("/metrics")
	public List<MetricsCollection> getMetrics() {
		log.debug("Records test:");
		return service.getMetrics();
	}
	
	@ResponseStatus(value = HttpStatus.OK)
	@GetMapping("/metrics/{id}")
	public Optional<MetricsCollection> findById(@PathVariable String id) {
		try {
			return service.findById(id);
		} catch(Exception error ){
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Metric not found", error);
		}
	}
  
  	@ResponseStatus(value = HttpStatus.CREATED)
	@PostMapping("/metrics")
	public String newMetric(@RequestBody CreateMetricRequest request) 
	{
		 String id= "";
		MappingTest test = new MappingTest();
		if(test.MappingTestMetric(request))
			 id = service.newMetric(request).getId();
		
		 return id;
	}
  
	@ResponseStatus(value = HttpStatus.NO_CONTENT)
	@DeleteMapping("/metrics/{id}")
	public void deleteMetric(@PathVariable String id) {
		service.deleteMetric(id);
	}
	
}

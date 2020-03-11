package com.metrics.service;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.metrics.controller.MetricsController;
import com.metrics.domain.CreateMetricRequest;
import com.metrics.model.MetricsCollection;

public class MappingTest {
	
	private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(MetricsController.class);
	public boolean MappingTestMetric(CreateMetricRequest metric) {
		boolean statusTest = false;
		String json ="";
	    ObjectMapper mapper = new ObjectMapper();
	    try {
	    	json = mapper.writerWithDefaultPrettyPrinter()
	                .writeValueAsString(metric);
	    }catch(Exception e) {
	    	log.debug("invalid json data structure");
	    }
	    
	    
	    
	    try {
	    	mapper.readValue(json, MetricsCollection.class);
	    	
	        statusTest = true;
	    }
	    catch (IOException error)
	    {
	    	throw new ResponseStatusException(
			          HttpStatus.BAD_REQUEST, "invalid json data structure");
	    }
	    
	    return statusTest;
	}
	
}

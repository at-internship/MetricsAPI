package com.metrics.service;


import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.metrics.domain.CreateMetricRequest;
import com.metrics.model.MetricsCollection;

public class MappingTest {
	
	
	public boolean MappingTestMetric(CreateMetricRequest metric) {
		boolean statusTest = false;
		String json ="";
	    ObjectMapper mapper = new ObjectMapper();
	    try {
	    	//Creating Json structure from CreateMetricRequest
	    	json = Functions.mapToJson(Functions.SetDefaultDataEmptyField(metric));
	    		    	
	    	mapper.readValue(json, MetricsCollection.class);
	    	
	    	
	    	//Verifying Date
	    	Functions.VerifyingDate(metric.getDate());
	    	
	    	//Verifying UUID id
	    	//Functions.VerifyingUUID(metric.getId());
	    	//Verifying UUID Evaluated_id
	    	//Functions.VerifyingUUID(metric.getEvaluated_id());
	    	//Verifying UUID Evaluator_id
	    	//Functions.VerifyingUUID(metric.getEvaluator_id());
	    	//Verifying UUID Sprint_id
	    	//Functions.VerifyingUUID(metric.getSprint_id());
	    	
	        statusTest = true;
	    	
	    }catch(Exception e) {
	    	throw new ResponseStatusException(
			          HttpStatus.BAD_REQUEST, "invalid json data structure");
	    }
	    
	    return statusTest;
	}
	
}
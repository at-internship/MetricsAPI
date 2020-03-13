package com.metrics.service;


import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.metrics.domain.CreateMetricRequest;
import com.metrics.model.MetricsCollection;
import com.metrics.model.blockers;
import com.metrics.model.metrics;
import com.metrics.model.proactive;
import com.metrics.model.retroactive;

public class MappingTest {
	
	
	public boolean MappingTestMetric(CreateMetricRequest metric) {
		boolean statusTest = false;
		String json ="";
	    ObjectMapper mapper = new ObjectMapper();
	    try {
	    	json = mapper.writerWithDefaultPrettyPrinter()
	                .writeValueAsString(SetDefaultDataEmptyField(metric));
	    	
	    	
	    	mapper.readValue(json, MetricsCollection.class);
	    	
	        statusTest = true;
	    	
	    }catch(Exception e) {
	    	throw new ResponseStatusException(
			          HttpStatus.BAD_REQUEST, "invalid json data structure");
	    }
	    
	    return statusTest;
	}
	private CreateMetricRequest SetDefaultDataEmptyField(CreateMetricRequest metric) {
		CreateMetricRequest collection = metric;
		
		 if (collection.getMetrics() == null) {
			 collection.setMetrics(new metrics(false,false,
					 			   new blockers(false,"Empty"),
					 			   new proactive(false, false,false,false),
					 			   new retroactive(false,"Empty")));
		 }
		 
		 if (collection.getMetrics().getBlockers() == null) {
			 collection.getMetrics().setBlockers(new blockers(false,"Empty"));
		 }
		 
		 if (collection.getMetrics().getProactive() == null) {
			 collection.getMetrics().setProactive(new proactive(false, false,false,false));
		 }

		 if (collection.getMetrics().getRetroactive() == null) {
			 collection.getMetrics().setRetroactive(new retroactive(false,"Empty"));
		 }

		 if(collection.getDate().isEmpty() || collection.getDate().isBlank()) {
			 collection.setDate("1000-01-01");
		 }
		 
		 if(collection.getType().isEmpty() || collection.getType().isBlank()) {
			 collection.setDate("Empty");
		 }
		 if(collection.getEvaluated_id().isEmpty() || collection.getEvaluated_id().isBlank()) {
			 throw new ResponseStatusException(
			          HttpStatus.BAD_REQUEST);
		 }
		 
		 if(collection.getEvaluator_id().isEmpty() || collection.getEvaluator_id().isBlank()) {
			 throw new ResponseStatusException(
			          HttpStatus.BAD_REQUEST);
		 }
		 if(collection.getSprint_id().isEmpty() || collection.getSprint_id().isBlank()) {
			 throw new ResponseStatusException(
			          HttpStatus.BAD_REQUEST);
		 }

		 if(collection.getMetrics().getBlockers().getComments().isEmpty() || collection.getDate().isBlank()) {
			 collection.getMetrics().getBlockers().setComments("Empty");
		 }
		 
		 if(collection.getMetrics().getRetroactive().getComments().isEmpty() || collection.getDate().isBlank()) {
			 collection.getMetrics().getRetroactive().setComments("Empty");
		 }
		 
		return collection;
	}
}
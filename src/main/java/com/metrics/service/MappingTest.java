package com.metrics.service;


import java.text.SimpleDateFormat;
import java.util.Date;

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
	    	 SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
	    	json = mapper.writerWithDefaultPrettyPrinter()
	                .writeValueAsString(SetDefaultDataEmptyField(metric));
	    	
	    	
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
	    	
	    	 Date dateTest = formatter.parse(metric.getDate());
	    	 metric.setDate(formatter.format(dateTest));
	    	 
	    	
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

		 if(collection.getDate().isEmpty()) {
			 collection.setDate("1000-01-01");
		 }
		 
		 if(collection.getType().isEmpty()) {
			 collection.setDate("Empty");
		 }
		 if(collection.getEvaluated_id().isEmpty()) {
			 throw new ResponseStatusException(
			          HttpStatus.BAD_REQUEST);
		 }
		 
		 if(collection.getEvaluator_id().isEmpty()) {
			 throw new ResponseStatusException(
			          HttpStatus.BAD_REQUEST);
		 }
		 if(collection.getSprint_id().isEmpty()) {
			 throw new ResponseStatusException(
			          HttpStatus.BAD_REQUEST);
		 }

		 if(collection.getMetrics().getBlockers().getComments().isEmpty()) {
			 collection.getMetrics().getBlockers().setComments("Empty");
		 }
		 
		 if(collection.getMetrics().getRetroactive().getComments().isEmpty()) {
			 collection.getMetrics().getRetroactive().setComments("Empty");
		 }
		 
		 
		return collection;
	}
}
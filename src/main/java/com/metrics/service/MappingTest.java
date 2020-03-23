package com.metrics.service;


import java.text.SimpleDateFormat;
import java.util.Date;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.metrics.MetricsApplication;
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
	    	MetricsApplication.logger.info("Starting date validation format");
	    	 SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
	    	json = mapper.writerWithDefaultPrettyPrinter()
	                .writeValueAsString(SetDefaultDataEmptyField(metric));
	    	
	    	MetricsApplication.logger.info("Validation integrity of the json");
	    	mapper.readValue(json, MetricsCollection.class);
	    	MetricsApplication.logger.info("Validating the data format");
	    	 Date dateTest = formatter.parse(metric.getDate());
	    	 metric.setDate(formatter.format(dateTest));
	    	 
	    	 MetricsApplication.logger.info("Data validation test passed..");
	        statusTest = true;
	    	
	    }catch(Exception e) {
	    	MetricsApplication.logger.error("Bad json structure");
	    	throw new ResponseStatusException(
			          HttpStatus.BAD_REQUEST, "invalid json data structure");
	    }
	    
	    return statusTest;
	}
	private CreateMetricRequest SetDefaultDataEmptyField(CreateMetricRequest metric) {
		MetricsApplication.logger.info("Starting the default value method");
		CreateMetricRequest collection = metric;
		MetricsApplication.logger.info("Setting by default all null values in metrics");
		 if (collection.getMetrics() == null) {
			 collection.setMetrics(new metrics(false,false,
					 			   new blockers(false,"Empty"),
					 			   new proactive(false, false,false,false),
					 			   new retroactive(false,"Empty")));
		 }
		 
		 if (collection.getMetrics().getBlockers() == null) {
			 MetricsApplication.logger.warn("Setting by default all null values in blockers");
			 collection.getMetrics().setBlockers(new blockers(false,"Empty"));
		 }
		 
		 if (collection.getMetrics().getProactive() == null) {
			 MetricsApplication.logger.warn("Setting by default all null values in proactive");
			 collection.getMetrics().setProactive(new proactive(false, false,false,false));
		 }
		 
		 if (collection.getMetrics().getRetroactive() == null) {
			 MetricsApplication.logger.warn("Setting by default all null values in retroactive");
			 collection.getMetrics().setRetroactive(new retroactive(false,"Empty"));
		 }
		 
		 if(collection.getDate().isEmpty()) {
			 MetricsApplication.logger.warn("Setting by default all null values in date");
			 collection.setDate("1000-01-01");
		 }
		 
		 if(collection.getType().isEmpty()) {
			 MetricsApplication.logger.warn("Setting by default all null values in type");
			 collection.setDate("Empty");
		 }
		 if(collection.getEvaluated_id().isEmpty()) {
			 MetricsApplication.logger.error("no Evaluated ID found");
			 throw new ResponseStatusException(
			          HttpStatus.BAD_REQUEST);
		 }
		 
		 if(collection.getEvaluator_id().isEmpty()) {
			 MetricsApplication.logger.error("no Evaluator ID found");
			 throw new ResponseStatusException(
			          HttpStatus.BAD_REQUEST);
		 }
		 if(collection.getSprint_id().isEmpty()) {
			 MetricsApplication.logger.error("no sprint ID found");
			 throw new ResponseStatusException(
			          HttpStatus.BAD_REQUEST);
		 }

		 if(collection.getMetrics().getBlockers().getComments().isEmpty()) {
			 MetricsApplication.logger.warn("Setting by default all null values in blockers");
			 collection.getMetrics().getBlockers().setComments("Empty");
		 }
		 
		 if(collection.getMetrics().getRetroactive().getComments().isEmpty()) {
			 MetricsApplication.logger.warn("Setting by default all null values in coments");
			 collection.getMetrics().getRetroactive().setComments("Empty");
		 }
		 
		 MetricsApplication.logger.info("All data validation and default setting done returning a TRUE bool from the test");
		return collection;
	}
}
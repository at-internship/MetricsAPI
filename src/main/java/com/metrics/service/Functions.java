package com.metrics.service;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.metrics.MetricsApplication;
import com.metrics.domain.CreateMetricRequest;
import com.metrics.model.MetricsCollection;
import com.metrics.model.blockers;
import com.metrics.model.metrics;
import com.metrics.model.proactive;
import com.metrics.model.retroactive;

public class Functions {
	public static  CreateMetricRequest MetricsCollectionToCreateMetricRequest(MetricsCollection metric) {
		CreateMetricRequest listIncoming = new CreateMetricRequest(metric.getId(), metric.getEvaluator_id(), metric.getEvaluated_id(), metric.getType(), metric.getDate(), metric.getSprint_id(), metric.getMetrics());
		return listIncoming;
	}
	public static void VerifyingUUID(String uuid) throws IllegalArgumentException {
		try {
			ObjectId.isValid(uuid);
		}catch(IllegalArgumentException error) {
			 throw new ResponseStatusException(
			          HttpStatus.BAD_REQUEST,
			          "The UUID has incorrect Format");
		}
	}
	
	public static void  IsDBEmpty (List<MetricsCollection> metrics) {
	MetricsApplication.logger.info("The method found " + metrics.size() + " records");
	if (metrics.size() == 0)
		throw new ResponseStatusException(HttpStatus.NO_CONTENT, "The DB have not records");
	}
	
	public static void  VerifyingAllTypesDatasIntoDB (List<MetricsCollection> metrics) {
	MappingTest testingList = new MappingTest();
	MetricsApplication.logger.info("Verifying records and validating type data");
		for (MetricsCollection metric: metrics) {
			if (!testingList.MappingTestMetric(Functions.MetricsCollectionToCreateMetricRequest(metric)))
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid structure metric " + metric.getId());
		}
	}
	
	public static void VerifyingDate(String date) {
		try {
			new SimpleDateFormat("yyyy-MM-dd").parse(date);
		}catch(Exception error) {
			 throw new ResponseStatusException(
			          HttpStatus.BAD_REQUEST,
			          "The Date has incorrect Format");
		}
	}
	
	public static Date stringToDate(String date) {
		Date parseDate = new Date();
		try {
			
			parseDate = new SimpleDateFormat("yyyy-MM-dd").parse(date);
		}catch(Exception error) {
			 throw new ResponseStatusException(
			          HttpStatus.BAD_REQUEST,
			          "The Date has incorrect Format");
		}
		return parseDate;
	}
	
	public static String mapToJson(Object obj) throws JsonProcessingException {
         ObjectMapper objectMapper = new ObjectMapper();
         return objectMapper.writeValueAsString(obj);
     }
	
	public static <T> T mapFromJson(String json, Class<T> clazz)
		      throws JsonParseException, JsonMappingException, IOException {
	      
	      ObjectMapper objectMapper = new ObjectMapper();
	      return objectMapper.readValue(json, clazz);
	}
	
	public static CreateMetricRequest SetDefaultDataEmptyField(CreateMetricRequest metric) {
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
	public static List<MetricsCollection> OrderByAscending(List<MetricsCollection> listMetric){
		List<MetricsCollection> listOrder = listMetric;
			Collections.sort(listOrder, new Comparator<MetricsCollection>() {
				@Override
				public int compare(MetricsCollection arg0, MetricsCollection arg1) {
					
					return arg0.getDate().compareTo(arg1.getDate());
				}
			});
		return listOrder;
	}
	public static List<MetricsCollection> OrderByDescending(List<MetricsCollection> listMetric){
		List<MetricsCollection> listOrder = listMetric;
		Collections.sort(listOrder, new Comparator<MetricsCollection>() {
			@Override
			public int compare(MetricsCollection arg0, MetricsCollection arg1) {
				
				return arg1.getDate().compareTo(arg0.getDate());
			}
		});
	return listOrder;
	}
}

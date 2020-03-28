package com.metrics.service;

import java.sql.Timestamp;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;
import com.metrics.model.SprintsCollection;
import com.metrics.model.UsersCollection;
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
	public static CreateMetricRequest MetricsCollectionToCreateMetricRequest(MetricsCollection metric) {
		CreateMetricRequest listIncoming = new CreateMetricRequest(metric.getId(), metric.getEvaluator_id(),
				metric.getEvaluated_id(), metric.getType(), metric.getDate(), metric.getSprint_id(),
				metric.getMetrics());
		return listIncoming;
	}

	public static List<MetricsCollection> filteringEmptyDates(List<MetricsCollection> listIncoming) {

		List<MetricsCollection> newList = new ArrayList<MetricsCollection>();

		for (MetricsCollection metric : listIncoming) {

			if (metric.getDate() != null) {
				if (VerifyingTimeStampValid(metric.getDate()))
					newList.add(metric);
			}
		}
		return newList;
	}

	public static void IsDBEmpty(List<MetricsCollection> metrics) {
		MetricsApplication.logger.info("The method found " + metrics.size() + " records");
		if (metrics.size() == 0)
			throw new ResponseStatusException(HttpStatus.NO_CONTENT, "The DB have not records");
	}

	public static void VerifyingAllTypesDatasIntoDB(List<MetricsCollection> metrics) {
		MetricsApplication.logger.info("Verifying records and validating type data");
		for (MetricsCollection metric : metrics) {
			if (!MappingTestMetric(MetricsCollectionToCreateMetricRequest(metric)))
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid structure metric " + metric.getId());
		}
	}

	public static boolean VerifyingTimeStampValid(String inputString) {
		SimpleDateFormat format = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSSSS");
		try {
			format.parse(inputString);
			return true;
		} catch (ParseException e) {
			return false;
		}
	}

	public static Date stringToDate(String dateIncoming) throws ParseException {
		Date date = null;
		try {
			date = new SimpleDateFormat("yyyy-MM-dd").parse(dateIncoming);
		} catch (ParseException error) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid structure date incoming ");
		}
		return date;
	}

	public static String mapToJson(Object obj) throws JsonProcessingException {
		ObjectMapper objectMapper = new ObjectMapper();
		return objectMapper.writeValueAsString(obj);
	}

	public static boolean MappingTestMetric(CreateMetricRequest metric) {
		boolean statusTest = false;
		String json = "";
		ObjectMapper mapper = new ObjectMapper();
		try {
			MetricsApplication.logger.info("Starting date validation format");
			json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(testMetricIntegrity(metric));

			MetricsApplication.logger.info("Validation integrity of the json");
			mapper.readValue(json, MetricsCollection.class);

			MetricsApplication.logger.info("Validating the date format");
			Functions.VerifyingTimeStampValid(metric.getDate());

			MetricsApplication.logger.info("Validating the ids formats");
			MetricsApplication.logger.info(metric.getEvaluator_id());
			Functions.VerifyingUUID(metric.getEvaluator_id());
			MetricsApplication.logger.info(metric.getEvaluated_id());
			Functions.VerifyingUUID(metric.getEvaluated_id());
			Functions.VerifyingUUID(metric.getSprint_id());

			MetricsApplication.logger.info("Data validation test passed..");
			statusTest = true;

		} catch (Exception e) {
		}
		MetricsApplication.logger.info("Return status.." + statusTest);
		return statusTest;
	}

	public static List<MetricsCollection> OrderByAscending(List<MetricsCollection> listMetric) {
		List<MetricsCollection> listOrder = listMetric;
		Collections.sort(listOrder, new Comparator<MetricsCollection>() {
			@Override
			public int compare(MetricsCollection o1, MetricsCollection o2) {
				Date date1 = null;
				Date date2 = null;
				int result;
				try {
					date1 = Functions.stringToDate(o1.getDate());
					date2 = Functions.stringToDate(o2.getDate());
				} catch (Exception e) {
				}
				if (date1.equals(date2)) {
					result = 0;
				}

				if (date1.after(date2)) {
					result = 1;
				} else {
					result = -1;
				}
				return result;

			}

		});
		return listOrder;
	}

	public static List<MetricsCollection> OrderByDescending(List<MetricsCollection> listMetric) {
		List<MetricsCollection> listOrder = listMetric;
		Collections.sort(listOrder, new Comparator<MetricsCollection>() {
			@Override
			public int compare(MetricsCollection o1, MetricsCollection o2) {
				Date date1 = null;
				Date date2 = null;
				int result;
				try {
					date1 = Functions.stringToDate(o1.getDate());
					date2 = Functions.stringToDate(o2.getDate());
				} catch (Exception e) {
				}
				if (date1.equals(date2)) {
					result = 0;
				}

				if (date1.after(date2)) {
					result = -1;
				} else {
					result = 1;
				}
				return result;

			}

		});
		return listOrder;
	}

	private static CreateMetricRequest testMetricIntegrity(CreateMetricRequest metric) {
		CreateMetricRequest collection = metric;

		if (collection.getMetrics() == null) {
			collection.setMetrics(new metrics(false, false, new blockers(false, "Empty"),
					new proactive(false, false, false, false), new retroactive(false, "Empty")));
		}

		if (collection.getMetrics().getBlockers() == null) {
			collection.getMetrics().setBlockers(new blockers(false, "Empty"));
		}

		if (collection.getMetrics().getProactive() == null) {
			collection.getMetrics().setProactive(new proactive(false, false, false, false));
		}

		if (collection.getMetrics().getRetroactive() == null) {
			collection.getMetrics().setRetroactive(new retroactive(false, "Empty"));
		}

		if (collection.getDate().isEmpty()) {
			collection.setDate("1000-01-01");
		}

		if (collection.getType().isEmpty()) {
			collection.setDate("Empty");
		}
		if (collection.getEvaluated_id().isEmpty()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}

		if (collection.getEvaluator_id().isEmpty()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
		if (collection.getSprint_id().isEmpty()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}

		if (collection.getMetrics().getBlockers().getComments().isEmpty()) {
			collection.getMetrics().getBlockers().setComments("Empty");
		}

		if (collection.getMetrics().getRetroactive().getComments().isEmpty()) {
			collection.getMetrics().getRetroactive().setComments("Empty");
		}

		return collection;
	}
	
	public static void VerifyingUUID(String uuid) {
		try {
			ObjectId.isValid(uuid);
		}catch(Exception error) {
			 throw new ResponseStatusException(
			          HttpStatus.BAD_REQUEST,
			          "The UUID has incorrect Format");
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
	
private static String getSprint()
	{
	    final String uri = "http://sprints-qa.us-east-2.elasticbeanstalk.com/sprints/";
	    RestTemplate restTemplate = new RestTemplate();
		return restTemplate.getForObject(uri, String.class);
	}
	
	public static boolean SprintsIdVerification(CreateMetricRequest request){
		boolean sprint_id = false;
		boolean result = false;
		MetricsApplication.logger.info("Generating container");
		try {
			String SprintsList = Functions.getSprint();
			MetricsApplication.logger.info(SprintsList);
    		SprintsCollection[] Sprints = Functions.mapFromJson(SprintsList, SprintsCollection[].class);
    		for(SprintsCollection sprint: Sprints) {
    			if(sprint.getId().equals(request.getSprint_id())) {
    				sprint_id = true;
    			}
    		}
    		if (sprint_id) {
    			result = true;
    		}else {
    			throw new ResponseStatusException(
  			          HttpStatus.BAD_REQUEST);
    		}
		}catch(Exception e){
			MetricsApplication.logger.error("Could not create object from API SPRINTS COLLECTIONS");
			throw new ResponseStatusException(
			          HttpStatus.BAD_REQUEST, "No SPRINT ID found");
		}
		
		return result;
	}
  
	private static String getUsersList() {
		final String uri = "http://sourcescusersapi-test.us-west-1.elasticbeanstalk.com/api/users/";
		RestTemplate restTemplate = new RestTemplate();
		
		return restTemplate.getForObject(uri, String.class);
	}
	
	public static boolean EvaluatorsIdVerification(CreateMetricRequest request){
		boolean evaluated_id = false, evaluator_id = false;
		boolean result = false;
		MetricsApplication.logger.info("Generating container");
		try {
			String UsersList = Functions.getUsersList();
			MetricsApplication.logger.info(UsersList);
    		UsersCollection[] Users = Functions.mapFromJson(UsersList, UsersCollection[].class);
    		for(UsersCollection user: Users) {
    			if(user.getUserId().equals(request.getEvaluated_id())) {
    				evaluated_id = true;
    			}
    			if(user.getUserId().equals(request.getEvaluator_id())) {
    				evaluator_id = true;
    			}
    		}
    		if (evaluated_id && evaluator_id) {
    			result = true;
    		}else {
    			throw new ResponseStatusException(
  			          HttpStatus.BAD_REQUEST);
    		}
		}catch(Exception e){
			MetricsApplication.logger.error("Could not create object from API USERS COLLECTIONS");
			throw new ResponseStatusException(
			          HttpStatus.BAD_REQUEST, "No evaluated or evaluator ID's found");
		}
		
		return result;
	}
}

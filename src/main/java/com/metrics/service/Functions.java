package com.metrics.service;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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

public class Functions {
	public static CreateMetricRequest MetricsCollectionToCreateMetricRequest(MetricsCollection metric) {
		CreateMetricRequest listIncoming = new CreateMetricRequest(metric.getId(), metric.getEvaluator_id(),
				metric.getEvaluated_id(), metric.getType(), metric.getDate(), metric.getSprint_id(),
				metric.getMetrics());
		return listIncoming;
	}

	public static MetricsCollection CreateMetricRequestToMetricsCollection(CreateMetricRequest metric) {
		MetricsCollection listIncoming = new MetricsCollection(metric.getId(), metric.getEvaluator_id(),
				metric.getEvaluated_id(), metric.getType(), metric.getDate(), metric.getSprint_id(),
				metric.getMetrics());
		return listIncoming;
	}

	public static void IsDBEmpty(List<MetricsCollection> metrics) {
		MetricsApplication.logger.info("The method found " + metrics.size() + " records");
		if (metrics.size() == 0)
			throw new ResponseStatusException(HttpStatus.NO_CONTENT, "The DB have not records");
	}

	public static void VerifyingAllTypesDatasIntoDB(List<MetricsCollection> metrics) {
		MetricsApplication.logger.info("Verifying records and validating type data");
		for (MetricsCollection metric : metrics) {
			testMetricIntegrity(MetricsCollectionToCreateMetricRequest(metric), 2);
		}
	}

	public static void VerifyingDateValid(String inputString) {
		SimpleDateFormat format = new java.text.SimpleDateFormat("yyyy-MM-dd");
		try {
			format.parse(inputString);
		} catch (ParseException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Date has incorrect Format");
		}
	}

	public static void VerifyingUUID(String uuid) {
		try {
			ObjectId.isValid(uuid);
		} catch (Exception error) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The UUID has incorrect Format");
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

	public static String mapToJson(Object obj) throws JsonProcessingException {
		ObjectMapper objectMapper = new ObjectMapper();
		return objectMapper.writeValueAsString(obj);
	}

	public static <T> T mapFromJson(String json, Class<T> clazz)
			throws JsonParseException, JsonMappingException, IOException {

		ObjectMapper objectMapper = new ObjectMapper();
		return objectMapper.readValue(json, clazz);
	}

	public static MetricsCollection datePUT;
	
	public static CreateMetricRequest testMetricIntegrity(CreateMetricRequest metric, int typeRequest) {

		if (metric.getId() != null && typeRequest != 2) {
			MetricsApplication.logger.info("id field must be null");
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "id field must be null");
		}
		
		if(typeRequest !=2 && (metric.getEvaluated_id().equals(metric.getEvaluator_id()))) {
			MetricsApplication.logger.info("Evaluator and Evaluated ID are the same");
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Evaluator and Evaluated ID are the same must not be equals.");
		}

		CreateMetricRequest collection = metric;
		ObjectMapper mapper = new ObjectMapper();
		try {
			MetricsApplication.logger.info("Validation integrity of the json");
			mapper.readValue(mapToJson(metric), CreateMetricRequest.class);
		} catch (Exception error) {
			MetricsApplication.logger.info("Json structure is not correct");
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Json structure is not correct");
		}

		MetricsApplication.logger.info("Verifying integrity of metrics objects");
		if (collection.getMetrics() == null) {
			MetricsApplication.logger.info("Metrics objects are null or have invalid structure");
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
					"Metrics objects are null or have invalid structure");
		}
		MetricsApplication.logger.info("Verifying integrity of blockers objects");
		if (collection.getMetrics().getBlockers() == null) {
			MetricsApplication.logger.info("Blockers objects are null or have invalid structure");
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
					"Blockers objects are null or have invalid structure");
		}
		MetricsApplication.logger.info("Verifying integrity of proactive object");
		if (collection.getMetrics().getProactive() == null) {
			MetricsApplication.logger.info("Proactive object is null or have invalid structure");
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
					"Proactive object is null or have invalid structure");
		}
		MetricsApplication.logger.info("Verifying integrity of retroactive object");
		if (collection.getMetrics().getRetroactive() == null) {
			MetricsApplication.logger.info("Retroactive object is null or have invalid structure");
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
					"Retroactive object is null or have invalid structure");
		}
		// 0 is POST Request
		// 1 is PUT Request
		// 2 is GET or another method
		MetricsApplication.logger.info("type of request " + typeRequest);
		try {

			if (collection.getDate() == null && typeRequest == 0) {
				Date date = new Date();
				DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
				metric.setDate(dateFormat.format(date));
			}
			if (collection.getDate() == null && typeRequest == 1) {
				String dateCopy = metric.getDate();
				metric.setDate(dateCopy);
			}
		} catch (Exception error) {

		}

		MetricsApplication.logger.info("Verifying integrity of type field");
		if (collection.getType().isEmpty()) {
			MetricsApplication.logger.info("Type field should not be null");
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Type field should not be null");
		}
		if (collection.getDate() != null) {
			if ((collection.getDate().isEmpty() && typeRequest == 0) || collection.getDate() == null) {
				Date date = new Date();
				DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
				metric.setDate(dateFormat.format(date));
			} else if (!collection.getDate().isEmpty() && typeRequest == 0) {
				MetricsApplication.logger.info("Verifying integrity of date field");
				VerifyingDateValid(metric.getDate());
			} else if (!collection.getDate().isEmpty() && typeRequest == 2) {
				MetricsApplication.logger.info("Verifying integrity of date field");
				VerifyingDateValid(metric.getDate());
			} else if (!collection.getDate().isEmpty() && typeRequest == 1) {
				MetricsApplication.logger.info("Date field can not be update");
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Date field can not be update");
			}
		}else {
			metric.setDate(datePUT.getDate());
		}

		MetricsApplication.logger.info("Verifying integrity of evaluated_id field");
		if (collection.getEvaluated_id().isEmpty()) {
			MetricsApplication.logger.info("evaluated_id field should not be null");
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "evaluated_id fiel should not be null");
		} else {
			try {
				VerifyingUUID(metric.getEvaluated_id());
			} catch (Exception error) {
				MetricsApplication.logger.info("evaluated_id field have not correct format");
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "evaluated_id have a invalid format ");
			}
		}
		MetricsApplication.logger.info("Verifying integrity of evaluator_id field");
		if (collection.getEvaluator_id().isEmpty()) {
			MetricsApplication.logger.info("evaluator_id field should not be null");
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "evaluator_id should not be null");
		} else {
			try {
				VerifyingUUID(metric.getEvaluator_id());
			} catch (Exception error) {
				MetricsApplication.logger.info("evaluator_id field have not correct format");
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "evaluator_id have a invalid format ");
			}
		}
		MetricsApplication.logger.info("Verifying integrity of sprint_id field");
		if (collection.getSprint_id().isEmpty()) {
			MetricsApplication.logger.info("sprint_id field should not be null");
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "sprint_id field should not be null");
		} else {
			try {
				VerifyingUUID(metric.getSprint_id());
			} catch (Exception error) {
				MetricsApplication.logger.info("sprint_id field have not correct format");
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "sprint_id have a invalid format ");
			}
		}

		return collection;
	}

	private static String getSprint() {
		final String uri = "http://sprints-qa.us-east-2.elasticbeanstalk.com/sprints/";
		RestTemplate restTemplate = new RestTemplate();
		return restTemplate.getForObject(uri, String.class);
	}

	public static boolean SprintsIdVerification(CreateMetricRequest request) {
		boolean sprint_id = false;
		boolean result = false;
		MetricsApplication.logger.info("Generating container");
		try {
			String SprintsList = Functions.getSprint();
			MetricsApplication.logger.info(SprintsList);
			SprintsCollection[] Sprints = Functions.mapFromJson(SprintsList, SprintsCollection[].class);
			for (SprintsCollection sprint : Sprints) {
				if (sprint.getId().equals(request.getSprint_id())) {
					sprint_id = true;
				}
			}
			if (sprint_id) {
				result = true;
			} else {
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
			}
		} catch (Exception e) {
			MetricsApplication.logger.error("Could not create object from API SPRINTS COLLECTIONS");
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No SPRINT ID found");
		}

		return result;
	}

	private static String getUsersList() {
		final String uri = "http://sourcescusersapi-test.us-west-1.elasticbeanstalk.com/api/users/";
		RestTemplate restTemplate = new RestTemplate();

		return restTemplate.getForObject(uri, String.class);
	}

	public static boolean EvaluatorsIdVerification(CreateMetricRequest request) {
		boolean evaluated_id = false, evaluator_id = false;
		boolean result = false;
		MetricsApplication.logger.info("Generating container");
		try {
			String UsersList = Functions.getUsersList();
			MetricsApplication.logger.info(UsersList);
			UsersCollection[] Users = Functions.mapFromJson(UsersList, UsersCollection[].class);
			for (UsersCollection user : Users) {
				if (user.getUserId().equals(request.getEvaluated_id())) {
					evaluated_id = true;
				}
				if (user.getUserId().equals(request.getEvaluator_id())) {
					evaluator_id = true;
				}
			}
			if (evaluated_id && evaluator_id) {
				result = true;
			} else {
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
			}
		} catch (Exception e) {
			MetricsApplication.logger.error("Could not create object from API USERS COLLECTIONS");
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No evaluated or evaluator ID's found");
		}

		return result;
	}
}

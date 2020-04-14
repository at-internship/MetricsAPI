package com.metrics.service;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TimeZone;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

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
			MetricsApplication.logger.info("Starting date format validation..");
			if (inputString.split("-").length != 3)
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Date has incorrect Format");
			String[] date = inputString.split("-");
			if (date[0].length() == 4 && date[1].length() == 2 && date[2].length() == 2) {
				if (!isWithinRange(stringToDate(inputString)))
					throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Date is out range");
				if (Integer.parseInt(date[2]) > 31) {
					MetricsApplication.logger.error("Incorrect day");
					throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Date has incorrect Format");
				}
				if (Integer.parseInt(date[1]) > 12) {
					MetricsApplication.logger.error("Incorrect month");
					throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Date has incorrect Format");
				}
				if (Integer.parseInt(date[0]) % 4 > 0) {
					MetricsApplication.logger.error("year inst leap");
					MetricsApplication.logger.error(Integer.parseInt(date[1]) == 2);
					MetricsApplication.logger.error(Integer.parseInt(date[2]) >= 29);
					if (Integer.parseInt(date[1]) == 2 && Integer.parseInt(date[2]) >= 29) {
						throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "This year isn't leap");
					}
				}
				format.parse(inputString);
			} else {
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Date has incorrect Format");
			}

		} catch (ParseException e) {
			MetricsApplication.logger.error("Date format validation failed!");
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Date has incorrect Format");
		}

	}

	public static boolean haveOnlyLetters(String uuid) {
		for (char letter : uuid.toCharArray()) {
			if (letter == '1' || letter == '2' || letter == '3' || letter == '4' || letter == '5' || letter == '6'
					|| letter == '7' || letter == '8' || letter == '9' || letter == '0') {
				return false;
			}
		}
		return true;
	}

	public static boolean haveOnlyNumbers(String uuid) {
		int counter = 0;
		for (char letter : uuid.toCharArray()) {
			if (letter == '1' || letter == '2' || letter == '3' || letter == '4' || letter == '5' || letter == '6'
					|| letter == '7' || letter == '8' || letter == '9' || letter == '0') {
				counter++;
			}
		}
		if (counter == uuid.length())
			return true;
		return false;
	}

	public static void VerifyingUUID(String uuid) {
		Pattern patt = Pattern.compile("[0-9a-f]{24}$");
		MetricsApplication.logger.error("Valiting id " + uuid);
		boolean validObjectId = patt.matcher(uuid).matches();
		if (!validObjectId || haveOnlyLetters(uuid) || haveOnlyNumbers(uuid)) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The UUID has incorrect Format");
		} else {
			MetricsApplication.logger.error("The uuid is valid");
		}
	}

	public static boolean VerifyingID(String uuid) {
		Pattern patt = Pattern.compile("^[a-zA-Z0-9]+$");
		MetricsApplication.logger.error("Valiting id " + uuid);

		boolean validObjectId = patt.matcher(uuid).matches();
		MetricsApplication.logger.error(validObjectId);
		if (!validObjectId)
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Special characters are not allowed");
		if (haveOnlyLetters(uuid) || haveOnlyNumbers(uuid)) {
			return false;
		} else {
			return true;
		}
	}

	static boolean isWithinRange(Date testDate) {
		Calendar cal = Calendar.getInstance();
		cal.setTimeZone(TimeZone.getTimeZone("CT"));
		cal.add(Calendar.YEAR, -10);
		Date pastDate = cal.getTime();
		cal.add(Calendar.YEAR, +20);
		Date futureDate = cal.getTime();
		return !(testDate.before(pastDate) || testDate.after(futureDate));
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
				return o1.getDate().compareTo(o2.getDate());
			}

		});
		return listOrder;
	}

	public static List<MetricsCollection> OrderByDescending(List<MetricsCollection> listMetric) {
		List<MetricsCollection> listOrder = listMetric;
		Collections.sort(listOrder, new Comparator<MetricsCollection>() {
			@Override
			public int compare(MetricsCollection o1, MetricsCollection o2) {

				return o2.getDate().compareTo(o1.getDate());

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
		if (metric.getEvaluated_id() != null && metric.getEvaluator_id() != null) {
			if (typeRequest != 2 && (metric.getEvaluated_id().equals(metric.getEvaluator_id()))) {
				MetricsApplication.logger.info("Evaluator and Evaluated ID are the same");
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
						"Evaluator and Evaluated ID are the same must not be equals.");
			}
		}

		if (metric.getType() == null) {
			MetricsApplication.logger.info("The field type should not be null");
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The field type should not be null");
		}

		if (metric.getMetrics() == null) {
			MetricsApplication.logger.info("The Metrics Object should not be null");
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The  Metrics Object  should not be null");
		}
		if (metric.getMetrics().getAttendance() == null) {
			MetricsApplication.logger.info("The Attendance field should not be null");
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The Attendance field should not be null");
		}

		if (metric.getMetrics().getCarried_over() == null) {
			MetricsApplication.logger.info("The Carried_over field should not be null");
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The Carried_over field should not be null");
		}

		if (metric.getMetrics().getBlockers() == null) {
			MetricsApplication.logger.info("The Blockers Object should not be null");
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The  Blockers Object  should not be null");
		}
		if (metric.getMetrics().getBlockers().getBlocked() == null) {
			MetricsApplication.logger.info("The Blocked field should not be null");
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The Blocked field should not be null");
		}
		if (metric.getMetrics().getBlockers().getComments() == null)
			metric.getMetrics().getBlockers().setComments("");

		if (metric.getMetrics().getProactive() == null) {
			MetricsApplication.logger.info("The Proactive Object should not be null");
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The  Proactive Object  should not be null");
		}
		if (metric.getMetrics().getProactive().getLooked_for_help() == null) {
			MetricsApplication.logger.info("The Looked_for_help field should not be null");
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The Looked_for_help field should not be null");
		}
		if (metric.getMetrics().getProactive().getProvided_help() == null) {
			MetricsApplication.logger.info("The Provided_help field should not be null");
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The Provided_help field should not be null");
		}
		if (metric.getMetrics().getProactive().getWorked_ahead() == null) {
			MetricsApplication.logger.info("The Worked_ahead field should not be null");
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The Worked_ahead field should not be null");
		}
		if (metric.getMetrics().getProactive().getShared_resources() == null) {
			MetricsApplication.logger.info("The Shared_resources field should not be null");
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The Shared_resources field should not be null");
		}

		if (metric.getMetrics().getRetroactive() == null) {
			MetricsApplication.logger.info("The Retroactive Object should not be null");
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The  Retroactive Object  should not be null");
		}
		if (metric.getMetrics().getRetroactive().getDelayed_looking_help() == null) {
			MetricsApplication.logger.info("The Delayed_looking_help field should not be null");
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
					"The Delayed_looking_help field should not be null");
		}
		if (metric.getMetrics().getRetroactive().getComments() == null) {
			metric.getMetrics().getRetroactive().setComments("");
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
			if (collection.getDate().isEmpty() && typeRequest == 0) {
				Date date = new Date();
				DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
				metric.setDate(dateFormat.format(date));
			} else if (!collection.getDate().isEmpty() && typeRequest == 0) {
				MetricsApplication.logger.info("Verifying integrity of date field");
				VerifyingDateValid(metric.getDate());
			} else if (!collection.getDate().isEmpty() && typeRequest == 1) {
				MetricsApplication.logger.info("Verifying integrity of date field");
				VerifyingDateValid(metric.getDate());
			}
		} else {
			metric.setDate(datePUT.getDate());
		}

		MetricsApplication.logger.info("Verifying integrity of evaluated_id field");
		if (collection.getEvaluated_id() == null) {
			MetricsApplication.logger.info("evaluated_id field should not be null");
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "evaluated_id should not be null");
		} else if (collection.getEvaluated_id().isEmpty()) {
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
		if (collection.getEvaluator_id() == null) {
			MetricsApplication.logger.info("evaluator_id field should not be null");
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "evaluator_id should not be null");
		} else if (collection.getEvaluator_id().isEmpty()) {
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
		if (collection.getSprint_id() == null) {
			MetricsApplication.logger.info("sprint_id field should not be null");
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "sprint_id field should not be null");
		} else if (collection.getSprint_id().isEmpty()) {
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

	public static boolean ifSprintExist(String id) {
		boolean response = false;
		final String uri = "http://sprints-qa.us-east-2.elasticbeanstalk.com/sprints/" + id;
		RestTemplate restTemplate = new RestTemplate();
		try {
			if (!restTemplate.getForObject(uri, String.class).isEmpty())
				response = true;
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.CONFLICT, "sprint_id given does not found");
		}
		return response;
	}

	public static boolean ifUserExist(String id) {
		boolean response = false;
		final String uri = "http://sourcescusersapi-test.us-west-1.elasticbeanstalk.com/api/users/" + id;
		RestTemplate restTemplate = new RestTemplate();
		try {
			if (!restTemplate.getForObject(uri, String.class).isEmpty())
				response = true;
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.CONFLICT, "user_id given does not found");
		}
		return response;
	}

	static boolean userSendPage;
	static boolean userSendSize;
	static boolean userSendStartDate;
	static boolean userSendEndDate;

	public static void checkPaginationParams(HttpServletRequest request) {
		userSendPage = false;
		userSendSize = false;
		request.getParameterMap().entrySet().forEach(entry -> {
			String param = entry.getKey();
			String paramValue = request.getParameter(param);
			if ((param.contains("page"))) {
				if (paramValue != "") {

					userSendPage = true;
				}
			} else if ((param.contains("size"))) {
				if (paramValue != "") {

					userSendSize = true;
				}
			}

		});
		if (userSendPage && !userSendSize) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
					"size value field is required when page is given");
		}
		if (!userSendPage && userSendSize) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
					"page value field is required when size is given");
		}
	}

	public static void checkDateParams(HttpServletRequest request) {
		userSendStartDate = false;
		userSendEndDate = false;
		request.getParameterMap().entrySet().forEach(entry -> {
			String param = entry.getKey();
			String paramValue = request.getParameter(param);
			if ((param.contains("startDate"))) {
				if (paramValue != "") {

					userSendStartDate = true;
				}
			} else if ((param.contains("endDate"))) {
				if (paramValue != "") {

					userSendEndDate = true;
				}
			}

		});
		if (userSendStartDate && !userSendEndDate) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
					"endDate value field is required when startDate is given");
		}
		if (!userSendStartDate && userSendEndDate) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
					"startDate value field is required when endDate is given");
		}
	}

	static boolean isOnlyGet;

	public static boolean checkIsOnlyGet(HttpServletRequest request) {
		MetricsApplication.logger.info("Checking if is only a get");
		isOnlyGet = false;
		if (request.getParameterMap().entrySet().isEmpty()) {
			isOnlyGet = true;
		}
		MetricsApplication.logger.info("is a only get..> " + isOnlyGet);
		return isOnlyGet;
	}

	public static void checkParams(HttpServletRequest request, Set<String> allowedParams) {
		request.getParameterMap().entrySet().forEach(entry -> {
			String param = entry.getKey();
			String paramValue = request.getParameter(param);
			if (!allowedParams.contains(param)) {
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
						"An invalid request param  called " + param + " has been entered");
			} else if ((param.contains("evaluated_id") || param.contains("sprint_id")
					|| param.contains("evaluator_id"))) {
				if (paramValue == "") {
					throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
							"The id key  " + param + " can not be null or empty");
				}
			}

		});
	}
}

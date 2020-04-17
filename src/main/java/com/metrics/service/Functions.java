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
import java.util.Set;
import java.util.TimeZone;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.metrics.MetricsApplication;
import com.metrics.domain.CreateMetricRequest;
import com.metrics.model.MetricsCollection;
import com.metrics.service.ErrorHandler.HttpExceptionMessage;
import com.metrics.service.ErrorHandler.PathErrorMessage;
import com.metrics.service.ErrorHandler.TypeError;
import com.metrics.service.StaticFunctionsVariables.StaticVariables;

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
		if (metrics.size() == 0) {
			TypeError.httpErrorMessage(new Exception(), HttpStatus.NO_CONTENT.value(), HttpStatus.NO_CONTENT.name(),
					HttpExceptionMessage.DBIsEmpty204, PathErrorMessage.pathMetric);
			throw new ResponseStatusException(HttpStatus.NO_CONTENT);
		}
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
			MetricsApplication.logger.info("Starting date format validation.." + inputString);
			if (inputString.split("-").length != 3) {
				TypeError.httpErrorMessage(new Exception(), HttpStatus.BAD_REQUEST.value(),
						HttpStatus.BAD_REQUEST.name(), HttpExceptionMessage.DateInvalidFormat400,
						PathErrorMessage.pathMetric);
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
			}
			String[] date = inputString.split("-");
			if (date[0].length() == 4 && date[1].length() == 2 && date[2].length() == 2) {
				if (!isWithinRange(stringToDate(inputString))) {
					TypeError.httpErrorMessage(new Exception(), HttpStatus.BAD_REQUEST.value(),
							HttpStatus.BAD_REQUEST.name(), HttpExceptionMessage.DateInvalidRange400,
							PathErrorMessage.pathMetric);
					throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
				}
				if (Integer.parseInt(date[2]) > 31 && Integer.parseInt(date[2]) != 02) {
					MetricsApplication.logger.error("Incorrect day");
					TypeError.httpErrorMessage(new Exception(), HttpStatus.BAD_REQUEST.value(),
							HttpStatus.BAD_REQUEST.name(), HttpExceptionMessage.DateInvalidDay400,
							PathErrorMessage.pathMetric);
					throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
				}
				if (Integer.parseInt(date[2]) > 29 && Integer.parseInt(date[1]) == 02
						&& Integer.parseInt(date[0]) % 4 == 0) {
					MetricsApplication.logger.error("Incorrect day to leap year");
					TypeError.httpErrorMessage(new Exception(), HttpStatus.BAD_REQUEST.value(),
							HttpStatus.BAD_REQUEST.name(), HttpExceptionMessage.DateYearIsLeap400,
							PathErrorMessage.pathMetric);
					throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
				}
				if (Integer.parseInt(date[2]) > 28 && Integer.parseInt(date[1]) == 02
						&& Integer.parseInt(date[0]) % 4 > 0) {
					MetricsApplication.logger.error("Incorrect day to Month");
					TypeError.httpErrorMessage(new Exception(), HttpStatus.BAD_REQUEST.value(),
							HttpStatus.BAD_REQUEST.name(), HttpExceptionMessage.DateYearIsNotLeap400,
							PathErrorMessage.pathMetric);
					throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
				}
				if (Integer.parseInt(date[1]) > 12) {
					MetricsApplication.logger.error("Incorrect month");
					TypeError.httpErrorMessage(new Exception(), HttpStatus.BAD_REQUEST.value(),
							HttpStatus.BAD_REQUEST.name(), HttpExceptionMessage.DateInvalidMonth400,
							PathErrorMessage.pathMetric);
					throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
				}
				format.parse(inputString);
			} else {
				TypeError.httpErrorMessage(new Exception(), HttpStatus.BAD_REQUEST.value(),
						HttpStatus.BAD_REQUEST.name(), HttpExceptionMessage.DateInvalidFormat400,
						PathErrorMessage.pathMetric);
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
			}

		} catch (ParseException error) {
			MetricsApplication.logger.error("Date format validation failed!");
			TypeError.httpErrorMessage(error, HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.name(),
					HttpExceptionMessage.DateInvalidFormat400, PathErrorMessage.pathMetric);
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
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
			TypeError.httpErrorMessage(new Exception(), HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.name(),
					HttpExceptionMessage.IDInvalid400, "/metric/" + uuid);
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		} else {
			MetricsApplication.logger.error("The id is valid");
		}
	}

	public static boolean VerifyingID(String uuid) {
		Pattern patt = Pattern.compile("^[a-zA-Z0-9]+$");
		MetricsApplication.logger.error("Valiting id " + uuid);

		boolean validObjectId = patt.matcher(uuid).matches();
		MetricsApplication.logger.error(validObjectId);
		if (!validObjectId) {
			TypeError.httpErrorMessage(new Exception(), HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.name(),
					HttpExceptionMessage.IdHasSpecialChar400, "/metric/" + uuid);
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
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
			TypeError.httpErrorMessage(error, HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.name(),
					HttpExceptionMessage.DateInvalidFormat400, PathErrorMessage.pathMetric);
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
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

	public static CreateMetricRequest testMetricIntegrity(CreateMetricRequest metric, int typeRequest) {
		// Verifying if the body request has id and it is PUT or POST
		if (metric.getId() != null && typeRequest != 2) {
			MetricsApplication.logger.info("id field must be null");
			TypeError.httpErrorMessage(new Exception(), HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.name(),
					HttpExceptionMessage.IdIsInBody400, PathErrorMessage.pathMetric);
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
		if (metric.getEvaluated_id() != null && metric.getEvaluator_id() != null) {
			if (typeRequest != 2 && (metric.getEvaluated_id().equals(metric.getEvaluator_id()))) {
				MetricsApplication.logger.info("Evaluator and Evaluated ID are the same");
				TypeError.httpErrorMessage(new Exception(), HttpStatus.BAD_REQUEST.value(),
						HttpStatus.BAD_REQUEST.name(), HttpExceptionMessage.SameIDs400, PathErrorMessage.pathMetric);
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
			}
		}
		MetricsApplication.logger.info("Verifying integrity of metrics objects and fields");
		if (metric.getType() == null) {
			MetricsApplication.logger.info("The field type should not be null");
			TypeError.httpErrorMessage(new Exception(), HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.name(),
					HttpExceptionMessage.FieldTypeNull400, PathErrorMessage.pathMetric);
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		} else if (metric.getType().isEmpty()) {
			MetricsApplication.logger.info("The field type should not be null");
			TypeError.httpErrorMessage(new Exception(), HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.name(),
					HttpExceptionMessage.FieldTypeNull400, PathErrorMessage.pathMetric);
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}

		if (metric.getMetrics() == null) {
			MetricsApplication.logger.info("The Metrics Object should not be null");
			TypeError.httpErrorMessage(new Exception(), HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.name(),
					HttpExceptionMessage.ObjectMetricNull400, PathErrorMessage.pathMetric);
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
		if (metric.getMetrics().getAttendance() == null) {
			MetricsApplication.logger.info("The Attendance field should not be null");
			TypeError.httpErrorMessage(new Exception(), HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.name(),
					HttpExceptionMessage.FieldAttendanceNull400, PathErrorMessage.pathMetric);
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The Attendance field should not be null");
		}

		if (metric.getMetrics().getCarried_over() == null) {
			MetricsApplication.logger.info("The Carried_over field should not be null");
			TypeError.httpErrorMessage(new Exception(), HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.name(),
					HttpExceptionMessage.FieldCarried_OverNull400, PathErrorMessage.pathMetric);
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}

		if (metric.getMetrics().getBlockers() == null) {
			MetricsApplication.logger.info("The Blockers Object should not be null");
			TypeError.httpErrorMessage(new Exception(), HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.name(),
					HttpExceptionMessage.ObjectBlockersNull400, PathErrorMessage.pathMetric);
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
		if (metric.getMetrics().getBlockers().getBlocked() == null) {
			MetricsApplication.logger.info("The Blocked field should not be null");
			TypeError.httpErrorMessage(new Exception(), HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.name(),
					HttpExceptionMessage.FieldBlockedNull400, PathErrorMessage.pathMetric);
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The Blocked field should not be null");
		}
		if (metric.getMetrics().getBlockers().getComments() == null)
			metric.getMetrics().getBlockers().setComments("");

		if (metric.getMetrics().getProactive() == null) {
			MetricsApplication.logger.info("The Proactive Object should not be null");
			TypeError.httpErrorMessage(new Exception(), HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.name(),
					HttpExceptionMessage.ObjectProactiveNull400, PathErrorMessage.pathMetric);
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
		if (metric.getMetrics().getProactive().getLooked_for_help() == null) {
			MetricsApplication.logger.info("The Looked_for_help field should not be null");
			TypeError.httpErrorMessage(new Exception(), HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.name(),
					HttpExceptionMessage.FieldLooked_for_help400, PathErrorMessage.pathMetric);
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
		if (metric.getMetrics().getProactive().getProvided_help() == null) {
			MetricsApplication.logger.info("The Provided_help field should not be null");
			TypeError.httpErrorMessage(new Exception(), HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.name(),
					HttpExceptionMessage.FieldProvided_help400, PathErrorMessage.pathMetric);
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
		if (metric.getMetrics().getProactive().getWorked_ahead() == null) {
			MetricsApplication.logger.info("The Worked_ahead field should not be null");
			TypeError.httpErrorMessage(new Exception(), HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.name(),
					HttpExceptionMessage.FieldWorked_ahead400, PathErrorMessage.pathMetric);
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
		if (metric.getMetrics().getProactive().getShared_resources() == null) {
			MetricsApplication.logger.info("The Shared_resources field should not be null");
			TypeError.httpErrorMessage(new Exception(), HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.name(),
					HttpExceptionMessage.FieldShared_resources400, PathErrorMessage.pathMetric);
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}

		if (metric.getMetrics().getRetroactive() == null) {
			MetricsApplication.logger.info("The Retroactive Object should not be null");
			TypeError.httpErrorMessage(new Exception(), HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.name(),
					HttpExceptionMessage.ObjectRetroactiveNull400, PathErrorMessage.pathMetric);
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
		if (metric.getMetrics().getRetroactive().getDelayed_looking_help() == null) {
			MetricsApplication.logger.info("The Delayed_looking_help field should not be null");
			TypeError.httpErrorMessage(new Exception(), HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.name(),
					HttpExceptionMessage.FieldDelayed_looking_helpNull400, PathErrorMessage.pathMetric);
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
		if (metric.getMetrics().getRetroactive().getComments() == null) {
			metric.getMetrics().getRetroactive().setComments("");
		}

		ObjectMapper mapper = new ObjectMapper();
		try {
			MetricsApplication.logger.info("Validation integrity of the json");
			mapper.readValue(mapToJson(metric), CreateMetricRequest.class);
		} catch (Exception error) {
			MetricsApplication.logger.info("Json structure is not correct");
			TypeError.httpErrorMessage(error, HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.name(),
					HttpExceptionMessage.JsonInvalidFormat400, PathErrorMessage.pathMetric);
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}

		// 0 is POST Request
		// 1 is PUT Request
		// 2 is GET or another method
		MetricsApplication.logger.info("type of request " + typeRequest);
		try {

			if (metric.getDate() == null && typeRequest == 0) {
				Date date = new Date();
				DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
				metric.setDate(dateFormat.format(date));
			}
			if (metric.getDate() == null && typeRequest == 1) {
				String dateCopy = metric.getDate();
				metric.setDate(dateCopy);
			}
		} catch (Exception error) {

		}

		MetricsApplication.logger.info("Verifying integrity of type field");
		if (metric.getDate() != null) {
			if (metric.getDate().isEmpty() && typeRequest == 0) {
				Date date = new Date();
				DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
				metric.setDate(dateFormat.format(date));
			} else if (!metric.getDate().isEmpty() && typeRequest == 0) {
				MetricsApplication.logger.info("Verifying integrity of date field");
				VerifyingDateValid(metric.getDate());
			} else if (!metric.getDate().isEmpty() && typeRequest == 1) {
				MetricsApplication.logger.info("Verifying integrity of date field");
				VerifyingDateValid(metric.getDate());
			}
		} else {
			metric.setDate(StaticVariables.datePUT.getDate());
		}

		MetricsApplication.logger.info("Verifying integrity of evaluated_id field");
		if (metric.getEvaluated_id() == null) {
			MetricsApplication.logger.info("evaluated_id field should not be null");
			TypeError.httpErrorMessage(new Exception(), HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.name(),
					HttpExceptionMessage.FieldEvaluated_idNull400, PathErrorMessage.pathMetric);
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		} else if (metric.getEvaluated_id().isEmpty()) {
			MetricsApplication.logger.info("evaluated_id field should not be null");
			TypeError.httpErrorMessage(new Exception(), HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.name(),
					HttpExceptionMessage.FieldEvaluated_idNull400, PathErrorMessage.pathMetric);
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		} else {
			VerifyingUUID(metric.getEvaluated_id());
		}
		MetricsApplication.logger.info("Verifying integrity of evaluator_id field");
		if (metric.getEvaluator_id() == null) {
			MetricsApplication.logger.info("evaluator_id field should not be null");
			TypeError.httpErrorMessage(new Exception(), HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.name(),
					HttpExceptionMessage.FieldEvaluator_idNull400, PathErrorMessage.pathMetric);
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		} else if (metric.getEvaluator_id().isEmpty()) {
			MetricsApplication.logger.info("evaluator_id field should not be null");
			TypeError.httpErrorMessage(new Exception(), HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.name(),
					HttpExceptionMessage.FieldEvaluator_idNull400, PathErrorMessage.pathMetric);
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		} else {
			VerifyingUUID(metric.getEvaluator_id());
		}
		MetricsApplication.logger.info("Verifying integrity of sprint_id field");
		if (metric.getSprint_id() == null) {
			MetricsApplication.logger.info("sprint_id field should not be null");
			TypeError.httpErrorMessage(new Exception(), HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.name(),
					HttpExceptionMessage.FieldSprint_id400, PathErrorMessage.pathMetric);
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		} else if (metric.getSprint_id().isEmpty()) {
			MetricsApplication.logger.info("sprint_id field should not be null");
			TypeError.httpErrorMessage(new Exception(), HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.name(),
					HttpExceptionMessage.FieldSprint_id400, PathErrorMessage.pathMetric);
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		} else {
			VerifyingUUID(metric.getSprint_id());
		}
		return metric;
	}

	public static boolean ifSprintExist(String id) {
		boolean response = false;
		final String uri = "http://sprints-qa.us-east-2.elasticbeanstalk.com/sprints/" + id;
		RestTemplate restTemplate = new RestTemplate();
		try {
			if (!restTemplate.getForObject(uri, String.class).isEmpty())
				response = true;
		} catch (Exception error) {
			TypeError.httpErrorMessage(error, HttpStatus.CONFLICT.value(), HttpStatus.CONFLICT.name(),
					HttpExceptionMessage.Sprint_idConflict409, "/metric/"+id);
			throw new ResponseStatusException(HttpStatus.CONFLICT);
		}
		return response;
	}

	public static boolean ifUserExist(String id, int typeId) {
		boolean response = false;
		final String uri = "http://sourcescusersapi-test.us-west-1.elasticbeanstalk.com/api/users/" + id;
		RestTemplate restTemplate = new RestTemplate();
		try {
			if (!restTemplate.getForObject(uri, String.class).isEmpty())
				response = true;
		} catch (Exception e) {
			if (typeId == 1) {
				TypeError.httpErrorMessage(new Exception(), HttpStatus.CONFLICT.value(), HttpStatus.CONFLICT.name(),
						HttpExceptionMessage.Evaluator_idConflict409, "/metric/"+id);
				throw new ResponseStatusException(HttpStatus.CONFLICT);
			} else if (typeId == 0) {
				TypeError.httpErrorMessage(new Exception(), HttpStatus.CONFLICT.value(), HttpStatus.CONFLICT.name(),
						HttpExceptionMessage.Evaluated_idConflict409, "/metric/"+id);
				throw new ResponseStatusException(HttpStatus.CONFLICT);
			}

		}
		return response;
	}

	public static void checkPaginationParams(HttpServletRequest request) {
		StaticVariables.userSendPage = false;
		StaticVariables.userSendSize = false;
		request.getParameterMap().entrySet().forEach(entry -> {
			String param = entry.getKey();
			String paramValue = request.getParameter(param);
			if ((param.contains("page"))) {
				if (paramValue != "") {

					StaticVariables.userSendPage = true;
				}
			} else if ((param.contains("size"))) {
				if (paramValue != "") {

					StaticVariables.userSendSize = true;
				}
			}

		});
		if (StaticVariables.userSendPage && !StaticVariables.userSendSize) {
			TypeError.httpErrorMessage(new Exception(), HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.name(),
					HttpExceptionMessage.SizeNull400, PathErrorMessage.pathMetric);
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
		if (!StaticVariables.userSendPage && StaticVariables.userSendSize) {
			TypeError.httpErrorMessage(new Exception(), HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.name(),
					HttpExceptionMessage.PageNull400, PathErrorMessage.pathMetric);
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
	}

	public static void checkDateParams(HttpServletRequest request) {
		StaticVariables.userSendStartDate = false;
		StaticVariables.userSendEndDate = false;
		request.getParameterMap().entrySet().forEach(entry -> {
			String param = entry.getKey();
			String paramValue = request.getParameter(param);
			if ((param.contains("startDate"))) {
				if (paramValue != "") {

					StaticVariables.userSendStartDate = true;
				}
			} else if ((param.contains("endDate"))) {
				if (paramValue != "") {

					StaticVariables.userSendEndDate = true;
				}
			}

		});
		if (StaticVariables.userSendStartDate && !StaticVariables.userSendEndDate) {
			TypeError.httpErrorMessage(new Exception(), HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.name(),
					HttpExceptionMessage.EndDateNull400, PathErrorMessage.pathMetric);
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
		if (!StaticVariables.userSendStartDate && StaticVariables.userSendEndDate) {
			TypeError.httpErrorMessage(new Exception(), HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.name(),
					HttpExceptionMessage.StartDateNull400, PathErrorMessage.pathMetric);
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
	}

	public static boolean checkIsOnlyGet(HttpServletRequest request) {
		MetricsApplication.logger.info("Checking if is only a get");
		StaticVariables.isOnlyGet = false;
		if (request.getParameterMap().entrySet().isEmpty()) {
			StaticVariables.isOnlyGet = true;
		}
		MetricsApplication.logger.info("is a only get..> " + StaticVariables.isOnlyGet);
		return StaticVariables.isOnlyGet;
	}

	public static void checkParams(HttpServletRequest request, Set<String> allowedParams) {
		request.getParameterMap().entrySet().forEach(entry -> {
			StaticVariables.parameterName = entry.getKey();
			String paramValue = request.getParameter(StaticVariables.parameterName);
			if (!allowedParams.contains(StaticVariables.parameterName)) {
				TypeError.httpErrorMessage(new Exception(), HttpStatus.BAD_REQUEST.value(),
						HttpStatus.BAD_REQUEST.name(), HttpExceptionMessage.InvalidParameter400,
						PathErrorMessage.pathMetric);
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
			} else if ((StaticVariables.parameterName.contains("evaluated_id")
					|| StaticVariables.parameterName.contains("sprint_id")
					|| StaticVariables.parameterName.contains("evaluator_id"))) {
				if (paramValue == "") {
					TypeError.httpErrorMessage(new Exception(), HttpStatus.BAD_REQUEST.value(),
							HttpStatus.BAD_REQUEST.name(), HttpExceptionMessage.ParameterNull400,
							PathErrorMessage.pathMetric);
					throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
				}
			}

		});
	}

}

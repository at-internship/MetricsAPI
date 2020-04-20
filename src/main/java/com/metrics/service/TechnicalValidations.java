package com.metrics.service;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.TimeZone;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
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

public class TechnicalValidations {
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
	
	public static String mapToJson(Object obj) throws JsonProcessingException {
		ObjectMapper objectMapper = new ObjectMapper();
		return objectMapper.writeValueAsString(obj);
	}

	public static <T> T mapFromJson(String json, Class<T> clazz)
			throws JsonParseException, JsonMappingException, IOException {

		ObjectMapper objectMapper = new ObjectMapper();
		return objectMapper.readValue(json, clazz);
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

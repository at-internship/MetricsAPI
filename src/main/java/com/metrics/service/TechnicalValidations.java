package com.metrics.service;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.regex.Pattern;


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
import com.metrics.service.ErrorHandler.HttpExceptionMessage;
import com.metrics.service.ErrorHandler.PathErrorMessage;
import com.metrics.service.ErrorHandler.TypeError;

public class TechnicalValidations {
	
	public static MetricsCollection CreateMetricRequestToMetricsCollection(CreateMetricRequest metric) {

		blockers blockers_string = new blockers();
		blockers_string.setBlocked(Boolean.parseBoolean(metric.getMetrics().getBlockers().getBlocked()));
		blockers_string.setComments(metric.getMetrics().getBlockers().getComments());

		proactive proactive_string = new proactive();
		proactive_string
				.setLooked_for_help(Boolean.parseBoolean(metric.getMetrics().getProactive().getLooked_for_help()));
		proactive_string.setProvided_help(Boolean.parseBoolean(metric.getMetrics().getProactive().getProvided_help()));
		proactive_string
				.setShared_resources(Boolean.parseBoolean(metric.getMetrics().getProactive().getShared_resources()));
		proactive_string.setWorked_ahead(Boolean.parseBoolean(metric.getMetrics().getProactive().getWorked_ahead()));

		retroactive retroactive_string = new retroactive();
		retroactive_string.setComments(metric.getMetrics().getRetroactive().getComments());
		retroactive_string.setDelayed_looking_help(
				Boolean.parseBoolean(metric.getMetrics().getRetroactive().getDelayed_looking_help()));

		metrics metric_string = new metrics();
		metric_string.setAttendance(Boolean.parseBoolean(metric.getMetrics().getAttendance()));
		metric_string.setBlockers(blockers_string);
		metric_string.setCarried_over(Boolean.parseBoolean(metric.getMetrics().getCarried_over()));
		metric_string.setProactive(proactive_string);
		metric_string.setRetroactive(retroactive_string);

		MetricsCollection listIncoming = new MetricsCollection(metric.getId(), metric.getEvaluator_id(),
				metric.getEvaluated_id(), metric.getType(), metric.getDate(), metric.getSprint_id(), metric_string);
		return listIncoming;
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
		if (verifyingIfNegativeNumber(uuid) < 0) {
			return true;
		}
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
	
	public static int verifyingIfNegativeNumber(String value) {
		int parse = 1;
		if (value.contains("-")) {
			try {
				parse = Integer.parseInt(value);
			}catch(NumberFormatException error) {
				parse = 0;
			}
		}
		return parse;
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
			TypeError.httpErrorMessage(HttpStatus.BAD_REQUEST, error, HttpExceptionMessage.DateInvalidFormat400,
					PathErrorMessage.pathMetric);
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

	public static void VerifyingUUID(String uuid) {
		Pattern patt = Pattern.compile("[0-9a-f]{24}$");
		MetricsApplication.logger.error("Valiting id " + uuid);
		boolean validObjectId = patt.matcher(uuid).matches();
		if (!validObjectId || TechnicalValidations.haveOnlyLetters(uuid)
				|| TechnicalValidations.haveOnlyNumbers(uuid)) {
			TypeError.httpErrorMessage(HttpStatus.BAD_REQUEST, new Exception(), HttpExceptionMessage.IDInvalid400,
					"/metrics/" + uuid);
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		} else {
			MetricsApplication.logger.error("The id is valid");
		}
	}
	
	public static void VerifyingUUID_Evaluator(String uuid, String id) {
		Pattern patt = Pattern.compile("[0-9a-f]{24}$");
		MetricsApplication.logger.error("Valiting evluator_id " + uuid);
		boolean validObjectId = patt.matcher(uuid).matches();
		if (!validObjectId || TechnicalValidations.haveOnlyLetters(uuid)
				|| TechnicalValidations.haveOnlyNumbers(uuid)) {
			TypeError.httpErrorMessage(HttpStatus.BAD_REQUEST, new Exception(), HttpExceptionMessage.EvaluatorIDInvalid400,
					"/metrics/" + id);
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		} else {
			MetricsApplication.logger.error("The evaluator_id is valid");
		}
	}
	
	public static void VerifyingUUID_Evaluated(String uuid, String id) {
		Pattern patt = Pattern.compile("[0-9a-f]{24}$");
		MetricsApplication.logger.error("Valiting evaluated_id " + uuid);
		boolean validObjectId = patt.matcher(uuid).matches();
		if (!validObjectId || TechnicalValidations.haveOnlyLetters(uuid)
				|| TechnicalValidations.haveOnlyNumbers(uuid)) {
			TypeError.httpErrorMessage(HttpStatus.BAD_REQUEST, new Exception(), HttpExceptionMessage.EvaluatedIDInvalid400,
					"/metrics/" + id);
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		} else {
			MetricsApplication.logger.error("The evaluated_id is valid");
		}
	}
	
	public static void VerifyingUUID_Sprint(String uuid, String id) {
		Pattern patt = Pattern.compile("[0-9a-f]{24}$");
		MetricsApplication.logger.error("Valiting sprint_id " + uuid);
		boolean validObjectId = patt.matcher(uuid).matches();
		if (!validObjectId || TechnicalValidations.haveOnlyLetters(uuid)
				|| TechnicalValidations.haveOnlyNumbers(uuid)) {
			TypeError.httpErrorMessage(HttpStatus.BAD_REQUEST, new Exception(), HttpExceptionMessage.SprintIDInvalid400,
					"/metrics/" + id);
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		} else {
			MetricsApplication.logger.error("The sprint_id is valid");
		}
	}


	public static boolean VerifyingID(String uuid) {
		Pattern patt = Pattern.compile("^[a-zA-Z0-9]+$");
		MetricsApplication.logger.info("Valiting id " + uuid);

		boolean validObjectId = patt.matcher(uuid).matches();
		MetricsApplication.logger.info(validObjectId);
		if (!validObjectId) {
			TypeError.httpErrorMessage(HttpStatus.BAD_REQUEST, new Exception(),
					HttpExceptionMessage.IdHasSpecialChar400, "/metrics/" + uuid);
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
		if (TechnicalValidations.haveOnlyLetters(uuid) || TechnicalValidations.haveOnlyNumbers(uuid)) {
			return false;
		} else {
			return true;
		}
	}

}

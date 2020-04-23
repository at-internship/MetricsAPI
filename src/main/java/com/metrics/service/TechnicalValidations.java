package com.metrics.service;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.TimeZone;
import java.util.regex.Pattern;

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
import com.metrics.model.blockers;
import com.metrics.model.blockersString;
import com.metrics.model.metrics;
import com.metrics.model.metricsString;
import com.metrics.model.proactive;
import com.metrics.model.proactiveString;
import com.metrics.model.retroactive;
import com.metrics.model.retroactiveString;
import com.metrics.service.ErrorHandler.HttpExceptionMessage;
import com.metrics.service.ErrorHandler.PathErrorMessage;
import com.metrics.service.ErrorHandler.TypeError;
import com.metrics.service.StaticFunctionsVariables.StaticVariables;

public class TechnicalValidations {
	public static CreateMetricRequest MetricsCollectionToCreateMetricRequest(MetricsCollection metric) {

		blockersString blockers_string = new blockersString();
		blockers_string.setBlocked(metric.getMetrics().getBlockers().getBlocked().toString());
		blockers_string.setComments(metric.getMetrics().getBlockers().getComments());

		proactiveString proactive_string = new proactiveString();
		proactive_string.setLooked_for_help(metric.getMetrics().getProactive().getLooked_for_help().toString());
		proactive_string.setProvided_help(metric.getMetrics().getProactive().getProvided_help().toString());
		proactive_string.setShared_resources(metric.getMetrics().getProactive().getShared_resources().toString());
		proactive_string.setWorked_ahead(metric.getMetrics().getProactive().getWorked_ahead().toString());

		retroactiveString retroactive_string = new retroactiveString();
		retroactive_string.setComments(metric.getMetrics().getRetroactive().getComments());
		retroactive_string
				.setDelayed_looking_help(metric.getMetrics().getRetroactive().getDelayed_looking_help().toString());

		metricsString metric_string = new metricsString();
		metric_string.setAttendance(metric.getMetrics().getAttendance().toString());
		metric_string.setBlockers(blockers_string);
		metric_string.setCarried_over(metric.getMetrics().getCarried_over().toString());
		metric_string.setProactive(proactive_string);
		metric_string.setRetroactive(retroactive_string);

		CreateMetricRequest listIncoming = new CreateMetricRequest(metric.getId(), metric.getEvaluator_id(),
				metric.getEvaluated_id(), metric.getType(), metric.getDate(), metric.getSprint_id(), metric_string);
		return listIncoming;
	}

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

	public static void VerifyingUUID(String uuid) {
		Pattern patt = Pattern.compile("[0-9a-f]{24}$");
		MetricsApplication.logger.error("Valiting id " + uuid);
		boolean validObjectId = patt.matcher(uuid).matches();
		if (!validObjectId || TechnicalValidations.haveOnlyLetters(uuid)
				|| TechnicalValidations.haveOnlyNumbers(uuid)) {
			TypeError.httpErrorMessage(new Exception(), HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.name(),
					HttpExceptionMessage.IDInvalid400, "/metrics/" + uuid);
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

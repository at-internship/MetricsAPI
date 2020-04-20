package com.metrics.service;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.metrics.MetricsApplication;
import com.metrics.domain.CreateMetricRequest;
import com.metrics.model.MetricsCollection;
import com.metrics.service.ErrorHandler.HttpExceptionMessage;
import com.metrics.service.ErrorHandler.PathErrorMessage;
import com.metrics.service.ErrorHandler.TypeError;
import com.metrics.service.StaticFunctionsVariables.StaticVariables;

public class ClientValidations {

	public static void VerifyingAllTypesDatasIntoDB(List<MetricsCollection> metrics) {
		MetricsApplication.logger.info("Verifying records and validating type data");
		for (MetricsCollection metric : metrics) {
			testMetricIntegrity(TechnicalValidations.MetricsCollectionToCreateMetricRequest(metric), 2);
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
				if (!TechnicalValidations.isWithinRange(TechnicalValidations.stringToDate(inputString))) {
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

	public static void VerifyingUUID(String uuid) {
		Pattern patt = Pattern.compile("[0-9a-f]{24}$");
		MetricsApplication.logger.error("Valiting id " + uuid);
		boolean validObjectId = patt.matcher(uuid).matches();
		if (!validObjectId || TechnicalValidations.haveOnlyLetters(uuid)
				|| TechnicalValidations.haveOnlyNumbers(uuid)) {
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
		if (TechnicalValidations.haveOnlyLetters(uuid) || TechnicalValidations.haveOnlyNumbers(uuid)) {
			return false;
		} else {
			return true;
		}
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
			mapper.readValue(TechnicalValidations.mapToJson(metric), CreateMetricRequest.class);
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

}

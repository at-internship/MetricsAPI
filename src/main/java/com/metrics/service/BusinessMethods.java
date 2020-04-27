package com.metrics.service;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

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

public class BusinessMethods {

	public static boolean ifSprintExist(String id) {
		boolean response = false;
		try {
			if (!ClientValidations.sprintsApi(id).isEmpty())
				response = true;
		} catch (Exception error) {
			TypeError.httpErrorMessage(HttpStatus.CONFLICT, error, HttpExceptionMessage.Sprint_idConflict409,
					"/metric/" + id);
			throw new ResponseStatusException(HttpStatus.CONFLICT);
		}
		return response;
	}

	public static boolean ifUserExist(String id, int typeId) {
		boolean response = false;
		try {
			if (!ClientValidations.usersApi(id).isEmpty())
				response = true;
		} catch (Exception e) {
			if (typeId == 1) {
				TypeError.httpErrorMessage(HttpStatus.CONFLICT, new Exception(),
						HttpExceptionMessage.Evaluator_idConflict409, "/metric/" + id);
				throw new ResponseStatusException(HttpStatus.CONFLICT);
			} else if (typeId == 0) {
				TypeError.httpErrorMessage(HttpStatus.CONFLICT, new Exception(),
						HttpExceptionMessage.Evaluated_idConflict409, "/metric/" + id);
				throw new ResponseStatusException(HttpStatus.CONFLICT);
			}
		}
		return response;
	}

	public static void VerifyingAllTypesDatasIntoDB(List<MetricsCollection> metrics) {
		MetricsApplication.logger.info("Verifying records and validating type data");
		for (MetricsCollection metric : metrics) {
			BusinessMethods.testMetricIntegrity(TechnicalValidations.MetricsCollectionToCreateMetricRequest(metric), 2);
		}
	}

	public static String VerifyingDateValid(String inputString, int typeRequest) {
		String[] date = new String[2];
		try {
			MetricsApplication.logger.info("Verifying if date has only letters " + inputString.split("-").length);
			if (TechnicalValidations.haveOnlyLetters(inputString)) {
				MetricsApplication.logger.info("Returning default date because " + inputString + " isn't allowed ");
				return "1000-01-01";
			}
			if (inputString.split("-").length < 3 || inputString.split("-").length > 3){
				MetricsApplication.logger.info("Returning default date becasue " + inputString +" isn't a date");
				return "1000-01-01";
			}
			
			date = inputString.split("-");
			if (date[0].length() != 4 && typeRequest != 2) {
				TypeError.httpErrorMessage(HttpStatus.BAD_REQUEST, new Exception(),
						HttpExceptionMessage.DateInvalidFormat400, PathErrorMessage.pathMetric);
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
			}
			if (date[0].length() == 4) {
				inputString = OrderingDate(date[0],date[1],date[2],typeRequest);
				
			} else if (date[2].length() == 4) {
				inputString = OrderingDate(date[2],date[1],date[0],typeRequest);
			}else if(date[0].length() != 4 || date[2].length() !=4) {
				MetricsApplication.logger.error("Incorrect year");
				TypeError.httpErrorMessage(HttpStatus.BAD_REQUEST, new Exception(),
						HttpExceptionMessage.DateInvalidYearFormat400, PathErrorMessage.pathMetric);
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
			}

		} catch (ParseException error) {
			TypeError.httpErrorMessage(HttpStatus.BAD_REQUEST, new Exception(),
					HttpExceptionMessage.DateInvalidFormat400, PathErrorMessage.pathMetric);
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
		MetricsApplication.logger.info("Returning new date " + inputString);
		return inputString;
	}

	private static String OrderingDate(String year, String month, String day, int typeRequest) throws ParseException {
		if(month.length() != 2) {
			MetricsApplication.logger.error("Incorrect month");
			TypeError.httpErrorMessage(HttpStatus.BAD_REQUEST, new Exception(),
					HttpExceptionMessage.DateInvalidMonthFormat400, PathErrorMessage.pathMetric);
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
		if(day.length() != 2) {
			MetricsApplication.logger.error("Incorrect day");
			TypeError.httpErrorMessage(HttpStatus.BAD_REQUEST, new Exception(),
					HttpExceptionMessage.DateInvalidDayFormat400, PathErrorMessage.pathMetric);
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
		MetricsApplication.logger.info("Starting date format validation.." + year + "-" + month + "-" + day);
		SimpleDateFormat format = new java.text.SimpleDateFormat("yyyy-MM-dd");
		if (typeRequest != 2) {
			if (!TechnicalValidations
					.isWithinRange(TechnicalValidations.stringToDate(year + "-" + month + "-" + day))) {
				TypeError.httpErrorMessage(HttpStatus.BAD_REQUEST, new Exception(),
						HttpExceptionMessage.DateInvalidRange400, PathErrorMessage.pathMetric);
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
			}
		}
		if (Integer.parseInt(day) > 31 && Integer.parseInt(day) != 02) {
			MetricsApplication.logger.error("Incorrect day");
			TypeError.httpErrorMessage(HttpStatus.BAD_REQUEST, new Exception(), HttpExceptionMessage.DateInvalidDay400,
					PathErrorMessage.pathMetric);
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
		if (Integer.parseInt(day) > 29 && Integer.parseInt(month) == 02 && Integer.parseInt(day) % 4 == 0) {
			MetricsApplication.logger.error("Incorrect day to leap year");
			TypeError.httpErrorMessage(HttpStatus.BAD_REQUEST, new Exception(), HttpExceptionMessage.DateYearIsLeap400,
					PathErrorMessage.pathMetric);
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
		if (Integer.parseInt(day) > 28 && Integer.parseInt(month) == 02 && Integer.parseInt(day) % 4 > 0) {
			MetricsApplication.logger.error("Incorrect day to Month");
			TypeError.httpErrorMessage(HttpStatus.BAD_REQUEST, new Exception(),
					HttpExceptionMessage.DateYearIsNotLeap400, PathErrorMessage.pathMetric);
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
		if (Integer.parseInt(month) > 12) {
			MetricsApplication.logger.error("Incorrect month");
			TypeError.httpErrorMessage(HttpStatus.BAD_REQUEST, new Exception(),
					HttpExceptionMessage.DateInvalidMonth400, PathErrorMessage.pathMetric);
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}

		format.parse(year + "-" + month + "-" + day);
		return year + "-" + month + "-" + day;
	}

	public static CreateMetricRequest testMetricIntegrity(CreateMetricRequest metric, int typeRequest) {
		// Verifying if the body request has id and it is PUT or POST
		if (metric.getId() != null && typeRequest != 2) {
			MetricsApplication.logger.error("id field must be null");
			TypeError.httpErrorMessage(HttpStatus.BAD_REQUEST, new Exception(), HttpExceptionMessage.IdIsInBody400,
					PathErrorMessage.pathMetric);
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
		if (metric.getEvaluated_id() != null && metric.getEvaluator_id() != null) {
			if (typeRequest != 2 && (metric.getEvaluated_id().equals(metric.getEvaluator_id()))) {
				MetricsApplication.logger.error("Evaluator and Evaluated ID are the same");
				TypeError.httpErrorMessage(HttpStatus.BAD_REQUEST, new Exception(), HttpExceptionMessage.SameIDs400,
						PathErrorMessage.pathMetric);
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
			}
		}
		MetricsApplication.logger.info("Verifying integrity of metrics objects and fields");
		if (metric.getType() == null) {
			MetricsApplication.logger.error("The field type should not be null");
			TypeError.httpErrorMessage(HttpStatus.BAD_REQUEST, new Exception(), HttpExceptionMessage.FieldTypeNull400,
					PathErrorMessage.pathMetric);
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		} else if (metric.getType().isEmpty()) {
			MetricsApplication.logger.error("The field type should not be null");
			TypeError.httpErrorMessage(HttpStatus.BAD_REQUEST, new Exception(), HttpExceptionMessage.FieldTypeNull400,
					PathErrorMessage.pathMetric);
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}

		if (metric.getMetrics() == null) {
			MetricsApplication.logger.error("The Metrics Object should not be null");
			TypeError.httpErrorMessage(HttpStatus.BAD_REQUEST, new Exception(),
					HttpExceptionMessage.ObjectMetricNull400, PathErrorMessage.pathMetric);
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
		if (metric.getMetrics().getAttendance() == null) {
			MetricsApplication.logger.error("The Attendance field should not be null");
			TypeError.httpErrorMessage(HttpStatus.BAD_REQUEST, new Exception(),
					HttpExceptionMessage.FieldAttendanceNull400, PathErrorMessage.pathMetric);
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The Attendance field should not be null");
		} else if (!metric.getMetrics().getAttendance().equals("true")) {
			if (!metric.getMetrics().getAttendance().equals("false")) {
				TypeError.httpErrorMessage(HttpStatus.BAD_REQUEST, new Exception(),
						HttpExceptionMessage.FieldAttendanceInvalid400, PathErrorMessage.pathMetric);
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The Blocked field should not be null");
			}

		}

		if (metric.getMetrics().getCarried_over() == null) {
			MetricsApplication.logger.error("The Carried_over field should not be null");
			TypeError.httpErrorMessage(HttpStatus.BAD_REQUEST, new Exception(),
					HttpExceptionMessage.FieldCarried_OverNull400, PathErrorMessage.pathMetric);
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		} else if (!metric.getMetrics().getCarried_over().equals("true")) {
			if (!metric.getMetrics().getCarried_over().equals("false")) {
				TypeError.httpErrorMessage(HttpStatus.BAD_REQUEST, new Exception(),
						HttpExceptionMessage.FieldCarried_OverInvalid400, PathErrorMessage.pathMetric);
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
			}

		}

		if (metric.getMetrics().getBlockers() == null) {
			MetricsApplication.logger.error("The Blockers Object should not be null");
			TypeError.httpErrorMessage(HttpStatus.BAD_REQUEST, new Exception(),
					HttpExceptionMessage.ObjectBlockersNull400, PathErrorMessage.pathMetric);
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
		if (metric.getMetrics().getBlockers().getBlocked() == null) {
			MetricsApplication.logger.error("The Blocked field should not be null");
			TypeError.httpErrorMessage(HttpStatus.BAD_REQUEST, new Exception(),
					HttpExceptionMessage.FieldBlockedNull400, PathErrorMessage.pathMetric);
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The Blocked field should not be null");
		} else if (!metric.getMetrics().getBlockers().getBlocked().equals("true")) {
			if (!metric.getMetrics().getBlockers().getBlocked().equals("false")) {
				TypeError.httpErrorMessage(HttpStatus.BAD_REQUEST, new Exception(),
						HttpExceptionMessage.FieldBlockedInvalid400, PathErrorMessage.pathMetric);
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
			}

		}
		if (metric.getMetrics().getBlockers().getComments() == null)
			metric.getMetrics().getBlockers().setComments("");

		if (metric.getMetrics().getProactive() == null) {
			MetricsApplication.logger.error("The Proactive Object should not be null");
			TypeError.httpErrorMessage(HttpStatus.BAD_REQUEST, new Exception(),
					HttpExceptionMessage.ObjectProactiveNull400, PathErrorMessage.pathMetric);
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
		if (metric.getMetrics().getProactive().getLooked_for_help() == null) {
			MetricsApplication.logger.error("The Looked_for_help field should not be null");
			TypeError.httpErrorMessage(HttpStatus.BAD_REQUEST, new Exception(),
					HttpExceptionMessage.FieldLooked_for_helpNull400, PathErrorMessage.pathMetric);
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		} else if (!metric.getMetrics().getProactive().getLooked_for_help().equals("true")) {
			if (!metric.getMetrics().getProactive().getLooked_for_help().equals("false")) {
				TypeError.httpErrorMessage(HttpStatus.BAD_REQUEST, new Exception(),
						HttpExceptionMessage.FieldLooked_for_helpInvalid400, PathErrorMessage.pathMetric);
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The Blocked field should not be null");
			}

		}
		if (metric.getMetrics().getProactive().getProvided_help() == null) {
			MetricsApplication.logger.error("The Provided_help field should not be null");
			TypeError.httpErrorMessage(HttpStatus.BAD_REQUEST, new Exception(),
					HttpExceptionMessage.FieldProvided_helpNull400, PathErrorMessage.pathMetric);
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		} else if (!metric.getMetrics().getProactive().getProvided_help().equals("true")) {
			if (!metric.getMetrics().getProactive().getProvided_help().equals("false")) {
				TypeError.httpErrorMessage(HttpStatus.BAD_REQUEST, new Exception(),
						HttpExceptionMessage.FieldProvided_helpInvalid400, PathErrorMessage.pathMetric);
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The Blocked field should not be null");
			}

		}
		if (metric.getMetrics().getProactive().getWorked_ahead() == null) {
			MetricsApplication.logger.error("The Worked_ahead field should not be null");
			TypeError.httpErrorMessage(HttpStatus.BAD_REQUEST, new Exception(),
					HttpExceptionMessage.FieldWorked_aheadNull400, PathErrorMessage.pathMetric);
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		} else if (!metric.getMetrics().getProactive().getWorked_ahead().equals("true")) {
			if (!metric.getMetrics().getProactive().getWorked_ahead().equals("false")) {
				TypeError.httpErrorMessage(HttpStatus.BAD_REQUEST, new Exception(),
						HttpExceptionMessage.FieldWorked_aheadInvalid400, PathErrorMessage.pathMetric);
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The Blocked field should not be null");
			}

		}
		if (metric.getMetrics().getProactive().getShared_resources() == null) {
			MetricsApplication.logger.error("The Shared_resources field should not be null");
			TypeError.httpErrorMessage(HttpStatus.BAD_REQUEST, new Exception(),
					HttpExceptionMessage.FieldShared_resourcesNull400, PathErrorMessage.pathMetric);
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		} else if (!metric.getMetrics().getProactive().getShared_resources().equals("true")) {
			if (!metric.getMetrics().getProactive().getShared_resources().equals("false")) {
				TypeError.httpErrorMessage(HttpStatus.BAD_REQUEST, new Exception(),
						HttpExceptionMessage.FieldShared_resourcesInvalid400, PathErrorMessage.pathMetric);
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The Blocked field should not be null");
			}

		}

		if (metric.getMetrics().getRetroactive() == null) {
			MetricsApplication.logger.error("The Retroactive Object should not be null");
			TypeError.httpErrorMessage(HttpStatus.BAD_REQUEST, new Exception(),
					HttpExceptionMessage.ObjectRetroactiveNull400, PathErrorMessage.pathMetric);
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
		if (metric.getMetrics().getRetroactive().getDelayed_looking_help() == null) {
			MetricsApplication.logger.error("The Delayed_looking_help field should not be null");
			TypeError.httpErrorMessage(HttpStatus.BAD_REQUEST, new Exception(),
					HttpExceptionMessage.FieldDelayed_looking_helpNull400, PathErrorMessage.pathMetric);
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		} else if (!metric.getMetrics().getRetroactive().getDelayed_looking_help().equals("true")) {
			if (!metric.getMetrics().getRetroactive().getDelayed_looking_help().equals("false")) {
				TypeError.httpErrorMessage(HttpStatus.BAD_REQUEST, new Exception(),
						HttpExceptionMessage.FieldDelayed_looking_helpInvalid400, PathErrorMessage.pathMetric);
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The Blocked field should not be null");
			}

		}
		if (metric.getMetrics().getRetroactive().getComments() == null) {
			metric.getMetrics().getRetroactive().setComments("");
		}

		ObjectMapper mapper = new ObjectMapper();
		try {
			MetricsApplication.logger.info("Validation integrity of the json");
			mapper.readValue(TechnicalValidations.mapToJson(metric), CreateMetricRequest.class);
		} catch (Exception error) {
			MetricsApplication.logger.error("Json structure is not correct");
			TypeError.httpErrorMessage(HttpStatus.BAD_REQUEST, error, HttpExceptionMessage.JsonInvalidFormat400,
					PathErrorMessage.pathMetric);
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}

		// 0 is POST Request
		// 1 is PUT Request
		// 2 is GET or another method
		MetricsApplication.logger.info("The date element has..: " + metric.getDate());
		MetricsApplication.logger.info("type of request " + typeRequest);
		try {

			if ((metric.getDate() == null || metric.getDate().isEmpty()) && typeRequest == 0) {
				MetricsApplication.logger.info("POST Assigning new date..: " + metric.getDate());
				Date date = new Date();
				DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
				metric.setDate(dateFormat.format(date));
			}
			if ((metric.getDate() == null || metric.getDate().isEmpty()) && typeRequest == 1) {
				MetricsApplication.logger.info("PUT Assigning new date..: " + metric.getDate());
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
				MetricsApplication.logger.info("Verifying integrity of date field of " + metric.getDate());
				VerifyingDateValid(metric.getDate(), 0);
			} else if (!metric.getDate().isEmpty() && typeRequest == 1) {
				MetricsApplication.logger.info("Verifying integrity of date field of " + metric.getDate());
				VerifyingDateValid(metric.getDate(), 1);
			}
		} else {
			metric.setDate(StaticVariables.datePUT.getDate());
		}

		MetricsApplication.logger.info("Verifying integrity of evaluated_id field");
		if (metric.getEvaluated_id() == null) {
			MetricsApplication.logger.error("evaluated_id field should not be null");
			TypeError.httpErrorMessage(HttpStatus.BAD_REQUEST, new Exception(),
					HttpExceptionMessage.FieldEvaluated_idNull400, PathErrorMessage.pathMetric);
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		} else if (metric.getEvaluated_id().isEmpty()) {
			MetricsApplication.logger.error("evaluated_id field should not be null");
			TypeError.httpErrorMessage(HttpStatus.BAD_REQUEST, new Exception(),
					HttpExceptionMessage.FieldEvaluated_idNull400, PathErrorMessage.pathMetric);
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		} else {
			TechnicalValidations.VerifyingUUID(metric.getEvaluated_id());
		}
		MetricsApplication.logger.info("Verifying integrity of evaluator_id field");
		if (metric.getEvaluator_id() == null) {
			MetricsApplication.logger.error("evaluator_id field should not be null");
			TypeError.httpErrorMessage(HttpStatus.BAD_REQUEST, new Exception(),
					HttpExceptionMessage.FieldEvaluator_idNull400, PathErrorMessage.pathMetric);
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		} else if (metric.getEvaluator_id().isEmpty()) {
			MetricsApplication.logger.error("evaluator_id field should not be null");
			TypeError.httpErrorMessage(HttpStatus.BAD_REQUEST, new Exception(),
					HttpExceptionMessage.FieldEvaluator_idNull400, PathErrorMessage.pathMetric);
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		} else {
			TechnicalValidations.VerifyingUUID(metric.getEvaluator_id());
		}
		MetricsApplication.logger.info("Verifying integrity of sprint_id field");
		if (metric.getSprint_id() == null) {
			MetricsApplication.logger.error("sprint_id field should not be null");
			TypeError.httpErrorMessage(HttpStatus.BAD_REQUEST, new Exception(), HttpExceptionMessage.FieldSprint_id400,
					PathErrorMessage.pathMetric);
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		} else if (metric.getSprint_id().isEmpty()) {
			MetricsApplication.logger.error("sprint_id field should not be null");
			TypeError.httpErrorMessage(HttpStatus.BAD_REQUEST, new Exception(), HttpExceptionMessage.FieldSprint_id400,
					PathErrorMessage.pathMetric);
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		} else {
			TechnicalValidations.VerifyingUUID(metric.getSprint_id());
		}
		return metric;
	}
}

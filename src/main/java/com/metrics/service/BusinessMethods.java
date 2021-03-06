package com.metrics.service;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.metrics.MetricsApplication;
import com.metrics.domain.CreateMetricRequest;
import com.metrics.service.ErrorHandler.HttpExceptionMessage;
import com.metrics.service.ErrorHandler.PathErrorMessage;
import com.metrics.service.ErrorHandler.TypeError;
import com.metrics.service.StaticFunctionsVariables.StaticVariables;

public class BusinessMethods {

	public static boolean ifSprintExist(String id, String metric_id) {
		boolean response = false;
		try {
			if (!ClientValidations.sprintsApi(id).isEmpty())
				response = true;
		} catch (Exception error) {
			TypeError.httpErrorMessage(HttpStatus.CONFLICT, error, HttpExceptionMessage.Sprint_idConflict409,
					"/metrics/" + metric_id);
			throw new ResponseStatusException(HttpStatus.CONFLICT);
		}
		return response;
	}

	public static boolean ifUserExist(String id, int typeId, String metric_id) {
		boolean response = false;
		try {
			if (!ClientValidations.usersApi(id).isEmpty())
				response = true;
		} catch (Exception e) {
			if (typeId == 1) {
				TypeError.httpErrorMessage(HttpStatus.CONFLICT, new Exception(),
						HttpExceptionMessage.Evaluator_idConflict409, "/metrics/" + metric_id);
				throw new ResponseStatusException(HttpStatus.CONFLICT);
			} else if (typeId == 0) {
				TypeError.httpErrorMessage(HttpStatus.CONFLICT, new Exception(),
						HttpExceptionMessage.Evaluated_idConflict409, "/metrics/" + metric_id);
				throw new ResponseStatusException(HttpStatus.CONFLICT);
			}
		}
		return response;
	}

	public static String VerifyingDateValid(String inputString, int typeRequest, String id) {
		String[] date = new String[2];
		try {
			MetricsApplication.logger.info("Verifying if date has only letters " + inputString.split("-").length);
			if (TechnicalValidations.haveOnlyLetters(inputString)) {
				MetricsApplication.logger.info("Returning default date because " + inputString + " isn't allowed ");
				return "1000-01-01";
			}
			if (inputString.split("-").length < 3 || inputString.split("-").length > 3) {
				MetricsApplication.logger.info("Returning default date becasue " + inputString + " isn't a date");
				return "1000-01-01";
			}

			date = inputString.split("-");
			if (date[0].length() != 4 && typeRequest != 2) {
				TypeError.httpErrorMessage(HttpStatus.BAD_REQUEST, new Exception(),
						HttpExceptionMessage.DateInvalidFormat400, "/metrics/" + id);
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
			}
			if (date[0].length() == 4) {
				inputString = OrderingDate(date[0], date[1], date[2], typeRequest);

			} else if (date[2].length() == 4) {
				inputString = OrderingDate(date[2], date[1], date[0], typeRequest);
			} else if (date[0].length() != 4 || date[2].length() != 4) {
				MetricsApplication.logger.error("Incorrect year");
				TypeError.httpErrorMessage(HttpStatus.BAD_REQUEST, new Exception(),
						HttpExceptionMessage.DateInvalidYearFormat400, "/metrics/" + id);
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
			}
		} catch (Exception error) {
			MetricsApplication.logger.error("Error parsing date");
			TypeError.httpErrorMessage(HttpStatus.BAD_REQUEST, new Exception(),
					HttpExceptionMessage.DateInvalidFormat400, "/metrics/" + id);
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
		MetricsApplication.logger.info("Returning new date " + inputString);
		return inputString;
	}

	private static String OrderingDate(String year, String month, String day, int typeRequest) throws ParseException {
		if (month.length() != 2) {
			MetricsApplication.logger.error("Incorrect month");
			TypeError.httpErrorMessage(HttpStatus.BAD_REQUEST, new Exception(),
					HttpExceptionMessage.DateInvalidMonthFormat400, PathErrorMessage.pathMetric);
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
		if (day.length() != 2) {
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
		if (Integer.parseInt(day) > 29 && Integer.parseInt(month) == 02 && Integer.parseInt(year) % 4 == 0) {
			MetricsApplication.logger.error("Incorrect day to leap year");
			TypeError.httpErrorMessage(HttpStatus.BAD_REQUEST, new Exception(), HttpExceptionMessage.DateYearIsLeap400,
					PathErrorMessage.pathMetric);
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
		if (Integer.parseInt(day) > 28 && Integer.parseInt(month) == 02 && Integer.parseInt(year) % 4 > 0) {
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

	public static CreateMetricRequest testMetricIntegrity(CreateMetricRequest metric, int typeRequest, String id) {
		
		
		// Verifying if the body request has id and it is PUT or POST
		if (metric.getId() != null && typeRequest != 2) {
			MetricsApplication.logger.error("id field must be null");
			TypeError.httpErrorMessage(HttpStatus.BAD_REQUEST, new Exception(), HttpExceptionMessage.IdIsInBody400,
					"/metrics/" + id);
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
		if (metric.getEvaluated_id() != null && metric.getEvaluator_id() != null) {
			if (typeRequest != 2 && (metric.getEvaluated_id().equals(metric.getEvaluator_id()))) {
				MetricsApplication.logger.error("Evaluator and Evaluated ID are the same");
				TypeError.httpErrorMessage(HttpStatus.CONFLICT, new Exception(), HttpExceptionMessage.SameIDs400,
						"/metrics/" + id);
				throw new ResponseStatusException(HttpStatus.CONFLICT);
			}
		}
		MetricsApplication.logger.info("Verifying integrity of metrics objects and fields");
		if (metric.getType() == null) {
			MetricsApplication.logger.error("The field type should not be null");
			TypeError.httpErrorMessage(HttpStatus.BAD_REQUEST, new Exception(), HttpExceptionMessage.FieldTypeNull400,
					"/metrics/" + id);
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		} else if (metric.getType().isEmpty()) {
			MetricsApplication.logger.error("The field type should not be null");
			TypeError.httpErrorMessage(HttpStatus.BAD_REQUEST, new Exception(), HttpExceptionMessage.FieldTypeNull400,
					"/metrics/" + id);
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}

		if (metric.getMetrics() == null) {
			MetricsApplication.logger.error("The Metrics Object should not be null");
			TypeError.httpErrorMessage(HttpStatus.BAD_REQUEST, new Exception(),
					HttpExceptionMessage.ObjectMetricNull400, "/metrics/" + id);
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
		if (metric.getMetrics().getAttendance() == null) {
			MetricsApplication.logger.error("The Attendance field should not be null");
			TypeError.httpErrorMessage(HttpStatus.BAD_REQUEST, new Exception(),
					HttpExceptionMessage.FieldAttendanceNull400, "/metrics/" + id);
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		} else if (!metric.getMetrics().getAttendance().equals("true")) {
			if (!metric.getMetrics().getAttendance().equals("false")) {
				TypeError.httpErrorMessage(HttpStatus.BAD_REQUEST, new Exception(),
						HttpExceptionMessage.FieldAttendanceInvalid400, "/metrics/" + id);
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
			}

		}

		if (metric.getMetrics().getCarried_over() == null) {
			MetricsApplication.logger.error("The Carried_over field should not be null");
			TypeError.httpErrorMessage(HttpStatus.BAD_REQUEST, new Exception(),
					HttpExceptionMessage.FieldCarried_OverNull400, "/metrics/" + id);
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		} else if (!metric.getMetrics().getCarried_over().equals("true")) {
			if (!metric.getMetrics().getCarried_over().equals("false")) {
				TypeError.httpErrorMessage(HttpStatus.BAD_REQUEST, new Exception(),
						HttpExceptionMessage.FieldCarried_OverInvalid400, "/metrics/" + id);
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
			}

		}

		if (metric.getMetrics().getBlockers() == null) {
			MetricsApplication.logger.error("The Blockers Object should not be null");
			TypeError.httpErrorMessage(HttpStatus.BAD_REQUEST, new Exception(),
					HttpExceptionMessage.ObjectBlockersNull400, "/metrics/" + id);
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
		if (metric.getMetrics().getBlockers().getBlocked() == null) {
			MetricsApplication.logger.error("The Blocked field should not be null");
			TypeError.httpErrorMessage(HttpStatus.BAD_REQUEST, new Exception(),
					HttpExceptionMessage.FieldBlockedNull400, "/metrics/" + id);
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		} else if (!metric.getMetrics().getBlockers().getBlocked().equals("true")) {
			if (!metric.getMetrics().getBlockers().getBlocked().equals("false")) {
				TypeError.httpErrorMessage(HttpStatus.BAD_REQUEST, new Exception(),
						HttpExceptionMessage.FieldBlockedInvalid400, "/metrics/" + id);
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
			}

		}
		if (metric.getMetrics().getBlockers().getComments() == null)
			metric.getMetrics().getBlockers().setComments("");

		if (metric.getMetrics().getProactive() == null) {
			MetricsApplication.logger.error("The Proactive Object should not be null");
			TypeError.httpErrorMessage(HttpStatus.BAD_REQUEST, new Exception(),
					HttpExceptionMessage.ObjectProactiveNull400, "/metrics/" + id);
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
		if (metric.getMetrics().getProactive().getLooked_for_help() == null) {
			MetricsApplication.logger.error("The Looked_for_help field should not be null");
			TypeError.httpErrorMessage(HttpStatus.BAD_REQUEST, new Exception(),
					HttpExceptionMessage.FieldLooked_for_helpNull400, "/metrics/" + id);
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		} else if (!metric.getMetrics().getProactive().getLooked_for_help().equals("true")) {
			if (!metric.getMetrics().getProactive().getLooked_for_help().equals("false")) {
				TypeError.httpErrorMessage(HttpStatus.BAD_REQUEST, new Exception(),
						HttpExceptionMessage.FieldLooked_for_helpInvalid400, "/metrics/" + id);
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
			}

		}
		if (metric.getMetrics().getProactive().getProvided_help() == null) {
			MetricsApplication.logger.error("The Provided_help field should not be null");
			TypeError.httpErrorMessage(HttpStatus.BAD_REQUEST, new Exception(),
					HttpExceptionMessage.FieldProvided_helpNull400, "/metrics/" + id);
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		} else if (!metric.getMetrics().getProactive().getProvided_help().equals("true")) {
			if (!metric.getMetrics().getProactive().getProvided_help().equals("false")) {
				TypeError.httpErrorMessage(HttpStatus.BAD_REQUEST, new Exception(),
						HttpExceptionMessage.FieldProvided_helpInvalid400, "/metrics/" + id);
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
			}

		}
		if (metric.getMetrics().getProactive().getWorked_ahead() == null) {
			MetricsApplication.logger.error("The Worked_ahead field should not be null");
			TypeError.httpErrorMessage(HttpStatus.BAD_REQUEST, new Exception(),
					HttpExceptionMessage.FieldWorked_aheadNull400, "/metrics/" + id);
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		} else if (!metric.getMetrics().getProactive().getWorked_ahead().equals("true")) {
			if (!metric.getMetrics().getProactive().getWorked_ahead().equals("false")) {
				TypeError.httpErrorMessage(HttpStatus.BAD_REQUEST, new Exception(),
						HttpExceptionMessage.FieldWorked_aheadInvalid400, "/metrics/" + id);
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
			}

		}
		if (metric.getMetrics().getProactive().getShared_resources() == null) {
			MetricsApplication.logger.error("The Shared_resources field should not be null");
			TypeError.httpErrorMessage(HttpStatus.BAD_REQUEST, new Exception(),
					HttpExceptionMessage.FieldShared_resourcesNull400, "/metrics/" + id);
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		} else if (!metric.getMetrics().getProactive().getShared_resources().equals("true")) {
			if (!metric.getMetrics().getProactive().getShared_resources().equals("false")) {
				TypeError.httpErrorMessage(HttpStatus.BAD_REQUEST, new Exception(),
						HttpExceptionMessage.FieldShared_resourcesInvalid400, "/metrics/" + id);
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
			}

		}

		if (metric.getMetrics().getRetroactive() == null) {
			MetricsApplication.logger.error("The Retroactive Object should not be null");
			TypeError.httpErrorMessage(HttpStatus.BAD_REQUEST, new Exception(),
					HttpExceptionMessage.ObjectRetroactiveNull400, "/metrics/" + id);
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
		if (metric.getMetrics().getRetroactive().getDelayed_looking_help() == null) {
			MetricsApplication.logger.error("The Delayed_looking_help field should not be null");
			TypeError.httpErrorMessage(HttpStatus.BAD_REQUEST, new Exception(),
					HttpExceptionMessage.FieldDelayed_looking_helpNull400, "/metrics/" + id);
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		} else if (!metric.getMetrics().getRetroactive().getDelayed_looking_help().equals("true")) {
			if (!metric.getMetrics().getRetroactive().getDelayed_looking_help().equals("false")) {
				TypeError.httpErrorMessage(HttpStatus.BAD_REQUEST, new Exception(),
						HttpExceptionMessage.FieldDelayed_looking_helpInvalid400, "/metrics/" + id);
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
			}

		}
		if (metric.getMetrics().getRetroactive().getComments() == null) {
			metric.getMetrics().getRetroactive().setComments("");
		}

		// 0 is POST Request
		// 1 is PUT Request
		// 2 is GET or another method
		MetricsApplication.logger.info("The date element has..: " + metric.getDate());
		MetricsApplication.logger.info("type of request " + typeRequest);

		if ((metric.getDate() == null || metric.getDate().isEmpty()) && typeRequest == 0) {
			MetricsApplication.logger.info("POST Assigning new date..: " + metric.getDate());
			Date date = new Date();
			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			metric.setDate(dateFormat.format(date));
		}
		if ((metric.getDate() == null || metric.getDate().isEmpty()) && typeRequest == 1) {
			MetricsApplication.logger.info("PUT Assigning new date..: " + StaticVariables.datePUT.getDate());
			String dateCopy = StaticVariables.datePUT.getDate();
			metric.setDate(dateCopy);
		}

		if (metric.getDate() != null) {
			if (!metric.getDate().isEmpty() && typeRequest == 0) {
				MetricsApplication.logger.info("Verifying integrity of date field of " + metric.getDate());
				VerifyingDateValid(metric.getDate(), 0, id);
			} else if (!metric.getDate().isEmpty() && typeRequest == 1) {
				MetricsApplication.logger.info("Verifying integrity of date field of " + metric.getDate());
				VerifyingDateValid(metric.getDate(), 1, id);
			}
		}

		MetricsApplication.logger.info("Verifying integrity of evaluated_id field");
		if (metric.getEvaluated_id() == null || metric.getEvaluated_id().isEmpty()) {
			MetricsApplication.logger.error("evaluated_id field should not be null");
			TypeError.httpErrorMessage(HttpStatus.BAD_REQUEST, new Exception(),
					HttpExceptionMessage.FieldEvaluated_idNull400, "/metrics/" + id);
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		} else {
			TechnicalValidations.VerifyingUUID_Evaluated(metric.getEvaluated_id(), id);
		}
		MetricsApplication.logger.info("Verifying integrity of evaluator_id field");
		if (metric.getEvaluator_id() == null || metric.getEvaluator_id().isEmpty()) {
			MetricsApplication.logger.error("evaluator_id field should not be null");
			TypeError.httpErrorMessage(HttpStatus.BAD_REQUEST, new Exception(),
					HttpExceptionMessage.FieldEvaluator_idNull400, "/metrics/" + id);
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		} else {
			TechnicalValidations.VerifyingUUID_Evaluator(metric.getEvaluator_id(), id);
		}
		MetricsApplication.logger.info("Verifying integrity of sprint_id field");
		if (metric.getSprint_id() == null || metric.getSprint_id().isEmpty()) {
			MetricsApplication.logger.error("sprint_id field should not be null");
			TypeError.httpErrorMessage(HttpStatus.BAD_REQUEST, new Exception(), HttpExceptionMessage.FieldSprint_id400,
					"/metrics/" + id);
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		} else {
			TechnicalValidations.VerifyingUUID_Sprint(metric.getSprint_id(), id);
		}
		return metric;
	}
	
	public static String capitalizeWord(String str){  
	    String words[]=str.split("\\s");  
	    String capitalizeWord="";  
	    for(String w:words){  
	        String first=w.substring(0,1);  
	        String afterfirst=w.substring(1);  
	        capitalizeWord+=first.toUpperCase()+afterfirst+" ";  
	    }  
	    return capitalizeWord.trim();  
	}  
}

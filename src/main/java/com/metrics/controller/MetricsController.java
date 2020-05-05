package com.metrics.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import com.metrics.MetricsApplication;
import com.metrics.domain.CreateMetricRequest;
import com.metrics.model.MetricsCollection;
import com.metrics.service.BusinessMethods;
import com.metrics.service.MetricsServiceImpl;
import com.metrics.service.TechnicalValidations;
import com.metrics.service.ErrorHandler.HttpExceptionMessage;
import com.metrics.service.ErrorHandler.PathErrorMessage;
import com.metrics.service.ErrorHandler.TypeError;
import com.metrics.service.StaticFunctionsVariables.StaticVariables;

import java.util.List;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.DeleteMapping;

@RestController
public class MetricsController {
	@Autowired
	MetricsServiceImpl service;

	@ResponseStatus(value = HttpStatus.OK)
	@PutMapping("/metrics/{id}")
	public MetricsCollection updateMetric(@RequestBody CreateMetricRequest request, @PathVariable String id) {
		MetricsApplication.logger.info("Creating container");
		MetricsCollection resultMetric = new MetricsCollection();
		if (findById(id) != null) {
			StaticVariables.datePUT = service.findById(id);
			StaticVariables.evaluated_id = StaticVariables.datePUT.getEvaluated_id();
			StaticVariables.evaluator_id = StaticVariables.datePUT.getEvaluator_id();
			StaticVariables.sprint_id = StaticVariables.datePUT.getSprint_id();
		}
		TechnicalValidations.VerifyingUUID(id);
		BusinessMethods.testMetricIntegrity(request, 1);
		if (BusinessMethods.ifSprintExist(request.getSprint_id())
				&& BusinessMethods.ifUserExist(request.getEvaluated_id(), 0)
				&& BusinessMethods.ifUserExist(request.getEvaluator_id(), 1)) {
			MetricsApplication.logger.info("calling update service");
			resultMetric = service.updateMetric(request, id);
			MetricsApplication.logger.info("update successfull, returning updated object..");
		}
		return resultMetric;
	}

	@ResponseStatus(value = HttpStatus.OK)
	@GetMapping("/metrics")
	public List<MetricsCollection> getMetrics(@RequestParam(value = "page", defaultValue = "1") String pageStr,
			@RequestParam(value = "size", defaultValue = "2147483547") String sizeStr,
			@RequestParam(value = "startDate", defaultValue = "1000-01-01") String startDate,
			@RequestParam(value = "endDate", defaultValue = "1000-01-01") String endDate,
			@RequestParam(value = "evaluator_id", defaultValue = "") String evaluator_id,
			@RequestParam(value = "evaluated_id", defaultValue = "") String evaluated_id,
			@RequestParam(value = "sprint_id", defaultValue = "") String sprint_id,
			@RequestParam(value = "orderBy", defaultValue = "1") String orderByStr) {
		
		MetricsApplication.logger.error("Verifying startDate");
		startDate = BusinessMethods.VerifyingDateValid(startDate, 2);
		MetricsApplication.logger.error("Verifying endDate");
		endDate = BusinessMethods.VerifyingDateValid(endDate, 2);
		if (!startDate.equals("1000-01-01") && !endDate.equals("1000-01-01")) {
			if (startDate.compareTo(endDate) > 0) {
				MetricsApplication.logger.error("page or size is wrong");
				TypeError.httpErrorMessage(HttpStatus.BAD_REQUEST, new Exception(),
						HttpExceptionMessage.DateInvalidOrder400, PathErrorMessage.pathMetric);
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
			}
		}

		// Verifying only number
		MetricsApplication.logger.info("Verifying only number");
		if (!TechnicalValidations.haveOnlyNumbers(pageStr)) {
			MetricsApplication.logger.error("Not are only numbers");
			TypeError.httpErrorMessage(HttpStatus.BAD_REQUEST, new Exception(),
					HttpExceptionMessage.isOnlyNumberPageFail400, PathErrorMessage.pathMetric);
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
		if (!TechnicalValidations.haveOnlyNumbers(sizeStr)) {
			MetricsApplication.logger.error("Not are only numbers");
			TypeError.httpErrorMessage(HttpStatus.BAD_REQUEST, new Exception(),
					HttpExceptionMessage.isOnlyNumberSizeFail400, PathErrorMessage.pathMetric);
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
		if (!TechnicalValidations.haveOnlyNumbers(orderByStr)) {
			MetricsApplication.logger.error("Not are only numbers");
			TypeError.httpErrorMessage(HttpStatus.BAD_REQUEST, new Exception(),
					HttpExceptionMessage.isOnlyNumberOrderByFail400, PathErrorMessage.pathMetric);
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
		MetricsApplication.logger.info("Parse to int");
		int page = Integer.parseInt(pageStr);
		int orderBy = Integer.parseInt(orderByStr);
		int size = Integer.parseInt(sizeStr);

		// Verifying page and size
		MetricsApplication.logger.info("Verifying page and size");
		if (page < 1 && size < 1) {
			MetricsApplication.logger.error("page and size are wrong");
			TypeError.httpErrorMessage(HttpStatus.BAD_REQUEST, new Exception(),
					HttpExceptionMessage.InvalidPageAndSizeValue400, PathErrorMessage.pathMetric);
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		} else if (page < 1) {
			MetricsApplication.logger.error("page is wrong");
			TypeError.httpErrorMessage(HttpStatus.BAD_REQUEST, new Exception(),
					HttpExceptionMessage.InvalidPageValue400, PathErrorMessage.pathMetric);
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		} else if (size < 1) {
			MetricsApplication.logger.error("size is wrong");
			TypeError.httpErrorMessage(HttpStatus.BAD_REQUEST, new Exception(),
					HttpExceptionMessage.InvalidSizeValue400, PathErrorMessage.pathMetric);
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		} else {
			page = page - 1;
		}

		// Verifying orderBy
		MetricsApplication.logger.info("Verifying orderBy");
		if (orderBy > 1 || orderBy < 0) {
			TypeError.httpErrorMessage(HttpStatus.BAD_REQUEST, new Exception(),
					HttpExceptionMessage.OrderByInvalidValue400, PathErrorMessage.pathMetric);
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
		List<MetricsCollection> ListMetric = service.getMetricsFilter(sprint_id, evaluator_id, evaluated_id, startDate,
				endDate, page, size, orderBy);
		TechnicalValidations.IsDBEmpty(ListMetric);

		return ListMetric;
	}

	@ResponseStatus(value = HttpStatus.OK)
	@GetMapping("/metrics/{id}")
	public MetricsCollection findById(@PathVariable String id) {
		TechnicalValidations.VerifyingUUID(id);
		MetricsApplication.logger.info("Calling findById service");
		return service.findById(id);
	}

	@ResponseStatus(value = HttpStatus.CREATED)
	@PostMapping("/metrics")
	public String newMetric(@RequestBody CreateMetricRequest request) {
		String id = "";

		MetricsApplication.logger
				.info("Calling the data validation method and ID Validation for Evaluator and Evaluated ID");
		if (BusinessMethods.testMetricIntegrity(request, 0) != null
				&& BusinessMethods.ifSprintExist(request.getSprint_id())
				&& BusinessMethods.ifUserExist(request.getEvaluated_id(), 0)
				&& BusinessMethods.ifUserExist(request.getEvaluator_id(), 1)) {
			MetricsApplication.logger.info("data validation successfull,calling the newMetric service");
			id = service.newMetric(request).getId();
			MetricsApplication.logger.info("saving id into String to return");
		}
		MetricsApplication.logger.info("New metric created successfully returning the ID of the new metric");
		StaticVariables.id = id;
		return id;
	}

	@ResponseStatus(value = HttpStatus.NO_CONTENT)
	@DeleteMapping("/metrics/{id}")
	public void deleteMetric(@PathVariable String id) {
		TechnicalValidations.VerifyingUUID(id);
		MetricsApplication.logger.info("Calling deleteMetric service");
		service.deleteMetric(id);
		StaticVariables.id = "";
	}

}

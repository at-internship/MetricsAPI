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
import com.metrics.service.ClientValidations;
import com.metrics.service.MetricsServiceImpl;
import com.metrics.service.SortingMethods;
import com.metrics.service.TechnicalValidations;
import com.metrics.service.ErrorHandler.HttpExceptionMessage;
import com.metrics.service.ErrorHandler.PathErrorMessage;
import com.metrics.service.ErrorHandler.TypeError;
import com.metrics.service.StaticFunctionsVariables.StaticVariables;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.DeleteMapping;

@RestController
public class MetricsController {
	@Autowired
	MetricsServiceImpl service;

	@ResponseStatus(value = HttpStatus.OK)
	@PutMapping("/metrics/{id}")
	public MetricsCollection updateMetric(@RequestBody CreateMetricRequest request, @PathVariable String id) {
		StaticVariables.id = id;
		MetricsApplication.logger.info("Creating container");
		MetricsCollection resultMetric = new MetricsCollection();
		if (findById(id) != null) {
			StaticVariables.datePUT = service.findById(id).get();
			StaticVariables.id = id;
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
		StaticVariables.id = null;
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
		MetricsApplication.logger.info("Verifying if DB is empty");
		BusinessMethods.VerifyingDateValid(startDate, 2);
		BusinessMethods.VerifyingDateValid(endDate, 2);
		
		//Verifying only number
		if(!TechnicalValidations.haveOnlyNumbers(sizeStr) || !TechnicalValidations.haveOnlyNumbers(pageStr) || !TechnicalValidations.haveOnlyNumbers(orderByStr)) {
			MetricsApplication.logger.info("Not are only numbers");
			TypeError.httpErrorMessage(new Exception(), HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.name(),
					HttpExceptionMessage.isOnlyNumberFail400, PathErrorMessage.pathMetric);
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
			int page = Integer.parseInt(pageStr);
			int orderBy = Integer.parseInt(orderByStr);
			int size = Integer.parseInt(sizeStr);
		
		
		//Verifying page and size
		if (page < 1 || size < 1) {
			TypeError.httpErrorMessage(new Exception(), HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.name(),
					HttpExceptionMessage.InvalidPageOrSizeValue400, PathErrorMessage.pathMetric);
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		} else {
			page = page - 1;
		}
		
		//Verifying orderBy
		if (orderBy > 1 || orderBy < 0) {
			TypeError.httpErrorMessage(new Exception(), HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.name(),
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
	public Optional<MetricsCollection> findById(@PathVariable String id) {
		StaticVariables.id = id;

		try {

			MetricsApplication.logger.info("Calling findById service");
			return service.findById(id);
		} catch (Exception error) {
			MetricsApplication.logger.error("trying to call findById service but couldnt find the given ID");
			TypeError.httpErrorMessage(new Exception(), HttpStatus.NOT_FOUND.value(), HttpStatus.NOT_FOUND.name(),
					HttpExceptionMessage.IdNotFound404, "/metric/" + id);
			StaticVariables.id = null;
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		}
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
		StaticVariables.id = null;
		return id;
	}

	@ResponseStatus(value = HttpStatus.NO_CONTENT)
	@DeleteMapping("/metrics/{id}")
	public void deleteMetric(@PathVariable String id) {
		StaticVariables.id = id;
		MetricsApplication.logger.info("Calling deleteMetric service");
		service.deleteMetric(id);
		StaticVariables.id = null;
	}

}

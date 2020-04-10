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
import com.metrics.service.Functions;
import com.metrics.service.MetricsServiceImpl;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

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
		MetricsApplication.logger.info("Creating container");
		MetricsCollection resultMetric = new MetricsCollection();
		if (findById(id) != null) {
			Functions.datePUT = service.findById(id).get();
		}
		Functions.VerifyingUUID(id);
		Functions.testMetricIntegrity(request, 1);
		if (Functions.SprintsIdVerification(request) && Functions.EvaluatorsIdVerification(request)) {
			MetricsApplication.logger.info("calling update service");
			resultMetric = service.updateMetric(request, id);
			MetricsApplication.logger.info("update successfull, returning updated object..");
		}

		return resultMetric;
	}

	
	@ResponseStatus(value = HttpStatus.OK)
	@GetMapping("/metrics")
	public List<MetricsCollection> getMetrics(
			HttpServletRequest request,
			@RequestParam(value = "page", defaultValue = "1") int page,
			@RequestParam(value = "size", defaultValue = "1") int size,
			@RequestParam(value = "startDate", defaultValue = "1000-01-01") String startDate,
			@RequestParam(value = "endDate", defaultValue = "1000-01-01") String endDate,
			@RequestParam(value = "evaluator_id", defaultValue = "") String evaluator_id,
			@RequestParam(value = "evaluated_id", defaultValue = "") String evaluated_id,
			@RequestParam(value = "sprint_id", defaultValue = "") String sprint_id,
			@RequestParam(value = "orderBy", defaultValue = "-1") int orderBy
			)
	{
		
		MetricsApplication.logger.info(request.getQueryString());
		Set<String> allowedParams = new HashSet<String>();
		allowedParams.add("size");
		allowedParams.add("page"); 
		allowedParams.add("startDate"); 
		allowedParams.add("endDate"); 
		allowedParams.add("evaluator_id"); 
		allowedParams.add("evaluated_id");
		allowedParams.add("sprint_id");
		allowedParams.add("orderBy");
		Functions.checkParams(request,allowedParams);
		MetricsApplication.logger.info("Getting list of metrics");

		List<MetricsCollection> ListMetric = service.getMetrics();
		

		MetricsApplication.logger.info("Verifying if DB is empty");
		Functions.IsDBEmpty(ListMetric);

		MetricsApplication.logger.info("Verifying all types datas into DB");
		Functions.VerifyingAllTypesDatasIntoDB(ListMetric);

		MetricsApplication.logger.info("Setting false the variables withFilters");
		boolean withFilters = false;
		boolean withFiltersIds = false;
		boolean withFiltersIdEvaluator = false;
		boolean withFiltersIdEvaluated = false;
		boolean withFiltersIdSprint = false;
		boolean withFiltersDate = false;
		boolean withFiltersPagination = false;

		// Verifying orderBy size
		if (orderBy > 1)
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "use 0 ascend or 1 to descend");

		// Applying filter of pagination and applying order by ascendant
		if (orderBy == 1 && !withFilters) {
			MetricsApplication.logger.info("Applying Descending filter");
			withFilters = true;
			ListMetric = Functions.OrderByDescending(ListMetric);
		} else if (orderBy == 0 && !withFilters) {
			MetricsApplication.logger.info("Applying Ascending filter");
			withFilters = true;
			ListMetric = Functions.OrderByAscending(ListMetric);
		}
		if (!evaluator_id.equals("") || !evaluated_id.equals("") || !sprint_id.equals("")) {
			MetricsApplication.logger.info("Setting filter by Id's true");
			withFiltersIds = true;
		}
		if (!startDate.equals("1000-01-01") && !endDate.equals("1000-01-01")) {
			MetricsApplication.logger.info("Setting filter by date true");
			withFiltersDate = true;
		}
		if (page != 1 || size != 1) {
			MetricsApplication.logger.info("Setting filter by pagination true");
			withFiltersPagination = true;
		}
		// Applying filter by evaluator_id and applying order by ascendant
		if (withFiltersIds) {
			if (!evaluator_id.equals("")) {
				if (Functions.VerifyingID(evaluator_id)) {
					MetricsApplication.logger.info("Applying filter by evaluator_id and applying order by ascendant");
					MetricsApplication.logger.info("Setting true variable withFilters in evaluator_id");
					withFilters = true;
					MetricsApplication.logger.info("Running metodh getItemsFromIdFilter with evaluator_id");
					ListMetric = service.getItemsFromIdFilter(evaluator_id, ListMetric, 0);
					withFiltersIdEvaluator =  true;
				}
			} 
			// Applying filter by evaluated_id and applying order by ascendant
			if (!evaluated_id.equals("")) {
				if (Functions.VerifyingID(evaluated_id)) {
					MetricsApplication.logger.info("Applying filter by evaluated_id and applying order by ascendant");
					MetricsApplication.logger.info("Setting true variable withFilters in evaluated_id");
					withFilters = true;
					MetricsApplication.logger.info("Running metodh getItemsFromIdFilter with evaluated_id");
					ListMetric = service.getItemsFromIdFilter(evaluated_id, ListMetric, 1);
					withFiltersIdEvaluated = true;
				}
			} 
			//Applying filter by sprint_id and applying order by ascendant
			if (!sprint_id.equals("")) {
				if (Functions.VerifyingID(sprint_id)) {
					MetricsApplication.logger.info("Applying filter by sprint_id and applying order by ascendant");
					MetricsApplication.logger.info("Setting true variable withFilters in sprint_id");
					withFilters = true;
					MetricsApplication.logger.info("Running metodh getItemsFromIdFilter with sprint_id");
					ListMetric = service.getItemsFromIdFilter(sprint_id, ListMetric, 2);
					withFiltersIdSprint = true;
				}
			} 
			
			if (!withFiltersIdEvaluator && !withFiltersIdEvaluated && !withFiltersIdSprint){
				MetricsApplication.logger.info("Clearing list because evaluator id is wrong or missing");
				ListMetric.clear();
				return ListMetric;
			}
		}
		// Applying filter by date range and applying order by ascendant
		if (withFiltersDate) {
			try {

				withFilters = true;

				Functions.VerifyingDateValid(startDate);
				Functions.VerifyingDateValid(endDate);

				MetricsApplication.logger.info("Creating default value and parse to type date");
				Date defaultValueDate = Functions.stringToDate("1000-01-01");

				MetricsApplication.logger.info("Parse to type date the content of the incoming variable startDate");
				Date startDateLocal = Functions.stringToDate(startDate);
				MetricsApplication.logger.info(startDateLocal);

				MetricsApplication.logger.info("Parse to type date the content of the incoming variable endtDate");
				Date endDateLocal = Functions.stringToDate(endDate);
				MetricsApplication.logger.info(endDateLocal);

				if (startDateLocal.compareTo(defaultValueDate) > 0 && endDateLocal.compareTo(defaultValueDate) > 0) {
					MetricsApplication.logger.info("Applying filter by date range and applying order by ascendant");
					MetricsApplication.logger.info("Setting true variable withFilters in range dates");

					if (startDateLocal.after(endDateLocal)) {
						throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
					}

					ListMetric = service.getItemsFromDateRange(startDateLocal, endDateLocal, ListMetric);

				}

			} catch (Exception e) {
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "the start page is bigger than endPage");
			}
		}
		if (withFiltersPagination) {
			if (page > 0 && size > 0) {
				MetricsApplication.logger.info("Setting true variable withFilters in the pagination");
				withFilters = true;
				ListMetric = service.getAllMetricsPaginated(page, size, ListMetric);
			} else {
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "page or size have invalid number");
			}
		}

		// Not applying anything filter
		if (!withFilters) {
			MetricsApplication.logger.info("Not applying anything filter");
			MetricsApplication.logger.info("Returning list without any filters");
		}
		
		return ListMetric;
	}

	@ResponseStatus(value = HttpStatus.OK)
	@GetMapping("/metrics/{id}")
	public Optional<MetricsCollection> findById(@PathVariable String id) {
		try {
			
			MetricsApplication.logger.info("Calling findById service");
			return service.findById(id);
		} catch (Exception error) {
			MetricsApplication.logger.error("trying to call findById service but couldnt find the given ID");
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Metric not found", error);
		}
	}

	@ResponseStatus(value = HttpStatus.CREATED)
	@PostMapping("/metrics")
	public String newMetric(@RequestBody CreateMetricRequest request) {
		String id = "";
		MetricsApplication.logger
				.info("Calling the data validation method and ID Validation for Evaluator and Evaluated ID");
		if (Functions.testMetricIntegrity(request, 0) != null && Functions.SprintsIdVerification(request)
				&& Functions.EvaluatorsIdVerification(request)) {
			MetricsApplication.logger.info("data validation successfull,calling the newMetric service");
			id = service.newMetric(request).getId();
			MetricsApplication.logger.info("saving id into String to return");
		}
		MetricsApplication.logger.info("New metric created successfully returning the ID of the new metric");
		return id;
	}

	@ResponseStatus(value = HttpStatus.NO_CONTENT)
	@DeleteMapping("/metrics/{id}")
	public void deleteMetric(@PathVariable String id) {
		MetricsApplication.logger.info("Calling deleteMetric service");
		service.deleteMetric(id);
	}

}

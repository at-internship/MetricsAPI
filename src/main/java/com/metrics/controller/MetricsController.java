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
import java.util.List;
import java.util.Optional;
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
		Functions.VerifyingUUID(id);
		if(Functions.SprintsIdVerification(request) && Functions.EvaluatorsIdVerification(request)) {
			MetricsApplication.logger.info("calling update service");
			resultMetric = service.updateMetric(request, id);
			MetricsApplication.logger.info("update successfull, returning updated object..");
		}

		return resultMetric;
	}

	@ResponseStatus(value = HttpStatus.OK)
	@GetMapping("/metrics")
	public List<MetricsCollection> getMetrics(@RequestParam(value = "page", defaultValue = "-1") int page,
			@RequestParam(value = "size", defaultValue = "-1") int size,
			@RequestParam(value = "startDate", defaultValue = "1000-01-01") String startDate,
			@RequestParam(value = "endDate", defaultValue = "1000-01-01") String endDate,
			@RequestParam(value = "evaluator_id", defaultValue = "") String evaluator_id,
			@RequestParam(value = "evaluated_id", defaultValue = "") String evaluated_id,
			@RequestParam(value = "sprint_id", defaultValue = "") String sprint_id,
			@RequestParam(value = "orderBy", defaultValue = "-1") int orderBy) {

		MetricsApplication.logger.info("Getting list of metrics");

		List<MetricsCollection> ListMetric = service.getMetrics();

		MetricsApplication.logger.info("Verifying if DB is empty");
		Functions.IsDBEmpty(ListMetric);

		MetricsApplication.logger.info("Verifying all types datas into DB");
		Functions.VerifyingAllTypesDatasIntoDB(ListMetric);

		MetricsApplication.logger.info("Setting false the variable withFilters");
		boolean withFilters = false;

		// The method use the next numbers to apply order by
		// 0 = Filter by id
		// 1 = Filter by evaluator_id
		// 2 = Filter by evaluated_id
		// 3 = Filter by sprint_id;
		// The filter order is ascendant

		// Verifying orderBy size
		if (orderBy > 1)
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "use 0 ascend or 1 to descend");

		// Applying filter by evaluator_id and applying order by ascendant

		if (evaluator_id.compareTo("") > 0) {
			MetricsApplication.logger.info("Applying filter by evaluator_id and applying order by ascendant");
			MetricsApplication.logger.info("Setting true variable withFilters in evaluator_id");
			withFilters = true;
			MetricsApplication.logger.info("Running metodh getItemsFromIdFilter with evaluator_id");
			ListMetric = service.getItemsFromIdFilter(evaluator_id, ListMetric, 0, orderBy);

		}
		// Applying filter by evaluated_id and applying order by ascendant

		if (evaluated_id.compareTo("") > 0) {
			MetricsApplication.logger.info("Applying filter by evaluated_id and applying order by ascendant");
			MetricsApplication.logger.info("Setting true variable withFilters in evaluated_id");
			withFilters = true;
			MetricsApplication.logger.info("Running metodh getItemsFromIdFilter with evaluated_id");
			ListMetric = service.getItemsFromIdFilter(evaluated_id, ListMetric, 1, orderBy);

		}
		// Applying filter by sprint_id and applying order by ascendant
		if (sprint_id.compareTo("") > 0) {
			MetricsApplication.logger.info("Applying filter by sprint_id and applying order by ascendant");
			MetricsApplication.logger.info("Setting true variable withFilters in sprint_id");
			withFilters = true;
			MetricsApplication.logger.info("Running metodh getItemsFromIdFilter with sprint_id");
			ListMetric = service.getItemsFromIdFilter(sprint_id, ListMetric, 2, orderBy);

		}
		// Applying filter by date range and applying order by ascendant
		try {
			MetricsApplication.logger.info("Creating default value and parse to type date");
			Date defaultValueDate = Functions.stringToDate("1000-01-01");

			MetricsApplication.logger.info("Parse to type date the content of the incoming variable startDate");
			MetricsApplication.logger.info(startDate);
			Date startDateLocal = Functions.stringToDate(startDate);
			MetricsApplication.logger.info(startDateLocal);

			MetricsApplication.logger.info("Parse to type date the content of the incoming variable endtDate");
			MetricsApplication.logger.info(endDate);
			Date endDateLocal = Functions.stringToDate(endDate);
			MetricsApplication.logger.info(endDateLocal);

			if (startDateLocal.compareTo(defaultValueDate) > 0 && endDateLocal.compareTo(defaultValueDate) > 0) {
				MetricsApplication.logger.info("Applying filter by date range and applying order by ascendant");
				MetricsApplication.logger.info("Setting true variable withFilters in range dates");
				if (startDateLocal.after(endDateLocal)) {
					throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
				}
				withFilters = true;
				ListMetric = service.getItemsFromDateRange(startDateLocal, endDateLocal, ListMetric, orderBy);

			}
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "endDate is bigger than startDate");
		}

		// Applying filter of pagination and applying order by ascendant

		if (page != -1 && size != -1) {
			MetricsApplication.logger.info("Setting true variable withFilters in the pagination");
			withFilters = true;
			ListMetric = service.getAllMetricsPaginated(page, size, ListMetric, orderBy);
		}
		if (orderBy == 1  && !withFilters) {
			MetricsApplication.logger.info("Applying Descending filter");
			withFilters = true;
			ListMetric = Functions.OrderByDescending(ListMetric);
		} else if (orderBy == 0 && !withFilters) {
			MetricsApplication.logger.info("Applying Ascending filter");
			withFilters = true;
			ListMetric = Functions.OrderByAscending(ListMetric);
		}

		// Not applying anything filter
		if (!withFilters)
			MetricsApplication.logger.info("Not applying anything filter");

		MetricsApplication.logger.info("Return list with all metrics");
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
		MetricsApplication.logger.info("Calling the data validation method and ID Validation for Evaluator and Evaluated ID");
		Functions.VerifyingUUID(request.getEvaluated_id());
		if (Functions.testMetricIntegrity(request, 0) != null && Functions.SprintsIdVerification(request) && Functions.EvaluatorsIdVerification(request)) {
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

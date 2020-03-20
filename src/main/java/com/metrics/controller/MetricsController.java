package com.metrics.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import com.metrics.domain.CreateMetricRequest;
import com.metrics.model.MetricsCollection;
import com.metrics.service.Functions;
import com.metrics.service.MappingTest;
import com.metrics.service.MetricsServiceImpl;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.DeleteMapping;

@RestController
public class MetricsController {
	private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(MetricsController.class);
	@Autowired
	MetricsServiceImpl service;

	@ResponseStatus(value = HttpStatus.ACCEPTED)
	@PutMapping("/metrics/{id}")
	public MetricsCollection updateMetric(@RequestBody CreateMetricRequest request, @PathVariable String id) {
		MetricsCollection resultMetric = new MetricsCollection();
		log.debug("Update user request - id=" + id + " " + request.toString());

		resultMetric = service.updateMetric(request, id);

		return resultMetric;
	}

	
	
	@ResponseStatus(value = HttpStatus.OK)
	@GetMapping("/metrics")
	public List<MetricsCollection> getMetrics(@RequestParam(value = "start", defaultValue = "-1") int start,
			   								  @RequestParam(value = "size", defaultValue = "-1") int size,
											  @RequestParam(value = "startDate", defaultValue = "1000-01-01") String startDate,
											  @RequestParam(value = "endDate", defaultValue = "1000-01-01") String endDate,
											  @RequestParam(value = "startId", defaultValue = "") String startId,
											  @RequestParam(value = "endId", defaultValue = "") String endId,
											  @RequestParam(value = "filterdBy", defaultValue = "0") int filterdBy,
											  @RequestParam(value = "orderBy", defaultValue = "0") int orderBy){
		
		List<MetricsCollection> ListMetric = service.getMetrics();
		Date defaultValueDate = Functions.stringToDate("1000-01-01");
		Date startDateLocal = Functions.stringToDate(startDate);
		Date endDateLocal = Functions.stringToDate(endDate);
		boolean withFilters = false;
		
		System.out.println(startId.compareTo(""));
		System.out.println(endId.compareTo(""));
		//Applying filter by id with range of id's
		if(startId.compareTo("") > 0 && endId.compareTo("") > 0) {
			withFilters = true;
			System.out.println("Applying filter for ids");
			
			//The method use the next numbers to apply filter
			// 0 = Filter by id
			// 1 = Filter by evaluator_id
			// 2 = Filter by evaluated_id
			// 3 = Filter by sprint_id;
			
			//The method use the next numbers to apply order by
			// 0 = Filter by id
			// 1 = Filter by evaluator_id
			// 2 = Filter by evaluated_id
			// 3 = Filter by sprint_id;
			//The filter order is ascendant
			
			ListMetric = service.getSpecificItemsFromTypeIdRange(startId, endId, ListMetric, filterdBy, orderBy);
			System.out.println(ListMetric.size());
		}
		//Applying filter of pagination and applying order by ascendant
		if(start != -1 && size != -1) {
			withFilters = true;
			System.out.println("Applying filter with range");
			ListMetric = service.getAllMetricsPaginated(start, size, ListMetric, orderBy);
		}
		//Applying filter by date range and applying order by ascendant
		if(startDateLocal.compareTo(defaultValueDate) > 0 && endDateLocal.compareTo(defaultValueDate) > 0) {
			withFilters = true;
			ListMetric = service.getItemsFromDateRange(startDateLocal, endDateLocal, ListMetric, orderBy);
			System.out.println("Applying filter for dates");
			System.out.println(ListMetric.size());
			
		}
		//Not applying anything filter
		if (!withFilters){
			System.out.println("EApplying without filter");
			System.out.println(ListMetric.size());
			return service.getMetrics();
			
		}
		return ListMetric;
	}

	@ResponseStatus(value = HttpStatus.OK)
	@GetMapping("/metrics/{id}")
	public Optional<MetricsCollection> findById(@PathVariable String id) {
		try {
			return service.findById(id);
		} catch (Exception error) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Metric not found", error);
		}
	}

	@ResponseStatus(value = HttpStatus.CREATED)
	@PostMapping("/metrics")
	public String newMetric(@RequestBody CreateMetricRequest request) {
		String id = "";
		MappingTest test = new MappingTest();
		if (test.MappingTestMetric(request))
			id = service.newMetric(request).getId();

		return id;
	}

	@ResponseStatus(value = HttpStatus.NO_CONTENT)
	@DeleteMapping("/metrics/{id}")
	public void deleteMetric(@PathVariable String id) {
		service.deleteMetric(id);
	}

}

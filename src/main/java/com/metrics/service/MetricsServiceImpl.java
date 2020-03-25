package com.metrics.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import com.metrics.model.MetricsCollection;
import com.metrics.repository.MetricRepository;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.metrics.MetricsApplication;
import com.metrics.domain.CreateMetricRequest;
import ma.glasnost.orika.MapperFacade;
import org.springframework.web.server.ResponseStatusException;

@Service
@JsonIgnoreProperties(ignoreUnknown = true)
public class MetricsServiceImpl implements MetricsService {
	@Autowired
	private MapperFacade orikaMapperFacade;

	@Autowired
	MetricRepository repository;

	@Override
	public List<MetricsCollection> getMetrics() {
		MetricsApplication.logger.info("Creating list..");
		List<MetricsCollection> metricsCollection = new ArrayList<>();
		MetricsApplication.logger.info("Filling list with info");
		metricsCollection = repository.findAll();
		MetricsApplication.logger.info("Returning lists");
		return metricsCollection;
	}

	@Override
	public Optional<MetricsCollection> findById(String id) {
		if (!repository.existsById(id)) {
			MetricsApplication.logger.error("trying to find a metric but  did not found an ID");
			throw new HttpClientErrorException(HttpStatus.NOT_FOUND, "No metric found with the given id");
		}
		MetricsApplication.logger.info("Returning metric");
		return repository.findById(id);
	}

	@Override
	public MetricsCollection newMetric(CreateMetricRequest request) {
		MetricsApplication.logger.info("Generating container");
		MetricsCollection metric = new MetricsCollection();
		MetricsApplication.logger.info("mapping request object into metric object");
		metric = orikaMapperFacade.map(request, MetricsCollection.class);
		MetricsApplication.logger.info("Calling save method and saving  metric object into the data base");
		repository.save(metric);
		MetricsApplication.logger.info("Returning the metric object");
		return metric;
	}

	public void deleteMetric(String id) {
		if (repository.existsById(id)) {
			MetricsApplication.logger.info("Deleting metric");
			repository.deleteById(id);
		} else {
			MetricsApplication.logger.error("tried to delete metric but couldnt find an ID");
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "The given ID does not exist");
		}
	}

	@Override
	public MetricsCollection updateMetric(CreateMetricRequest request, String id) {
		if (!repository.existsById(id)) {
			MetricsApplication.logger.error("Tried to update metric but couldnt find the ID given");
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Metric not found");
		}
		MetricsApplication.logger.info("Creating metric object");
		MetricsCollection metric = new MetricsCollection();
		MetricsApplication.logger.info("calling data validation method");
		MappingTest test = new MappingTest();
		if (test.MappingTestMetric(request)) {
			MetricsApplication.logger.info("data validation test passed, saving new object");
			metric = orikaMapperFacade.map(request, MetricsCollection.class);
			MetricsApplication.logger.info("object created succesfully");
			metric.setId(id);
		} else {
			MetricsApplication.logger.error("Tried to create the object but didnt pass the valiation method");
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
		MetricsApplication.logger
				.info("Object created and validated successfully, saving into the database and returning the object");
		return repository.save(metric);
	}

	@Override
	public List<MetricsCollection> getAllMetricsPaginated(int start, int size, List<MetricsCollection> metrics,
			int orderBy) {

		List<MetricsCollection> listMetrics = metrics;
		MetricsApplication.logger.info("Applying filter selected to " + metrics.size() + " elements");
		if (orderBy == 0 && listMetrics.size() > 1) {
			MetricsApplication.logger.info("Applying filter selected to " + listMetrics.size() + " elements");
			listMetrics = Functions.OrderByAscending(listMetrics);
		} else if (orderBy == 1 && listMetrics.size() > 1) {
			MetricsApplication.logger.info("Applying filter selected to " + listMetrics.size() + " elements");
			listMetrics = Functions.OrderByDescending(listMetrics);
		}
		if (size > metrics.size())
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "the size especified is out of size of list ");
		if (start >= metrics.size())
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "the start especified is out of size of list ");
		MetricsApplication.logger.info("Return new paginated list, starting " + start + " to " + size);
		return listMetrics.subList(start, size);
	}

	@Override
	public List<MetricsCollection> getItemsFromDateRange(Timestamp startDate, Timestamp endDate,
			List<MetricsCollection> metrics, int orderBy) {

		MetricsApplication.logger.info("Creating list to save filter by range date");
		List<MetricsCollection> listMetricsFiltredDates = new ArrayList<MetricsCollection>();
		MetricsApplication.logger.info("Getting range dates");
		for (MetricsCollection metric : metrics) {
			try {
				if ((Functions.stringToTimestamp(metric.getDate()).after(startDate)
						|| Functions.stringToTimestamp(metric.getDate()).equals(startDate))
						&& Functions.stringToTimestamp(metric.getDate()).before(endDate)
						|| Functions.stringToTimestamp(metric.getDate()).equals(endDate)) {
					listMetricsFiltredDates.add(metric);
				}
			} catch (Exception e) {
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error to compare timestamp dates");
			}

		}
		// 0 = Filter by id
		// 1 = evaluator_id
		// 2 = evaluated_id
		// 3 = date;
		// 4 = sprint_id;
		if (orderBy == 0) {
			MetricsApplication.logger
					.info("Applying filter selected to " + listMetricsFiltredDates.size() + " elements");
			listMetricsFiltredDates = Functions.OrderByAscending(listMetricsFiltredDates);
		} else if (orderBy == 1) {
			MetricsApplication.logger
					.info("Applying filter selected to " + listMetricsFiltredDates.size() + " elements");
			listMetricsFiltredDates = Functions.OrderByDescending(listMetricsFiltredDates);
		}

		MetricsApplication.logger
				.info("Return new list with the metric matches with " + listMetricsFiltredDates.size() + " elements");
		return listMetricsFiltredDates;
	}

	@Override
	public List<MetricsCollection> getItemsFromIdFilter(String id, List<MetricsCollection> metrics, int typeId,
			int orderBy) {

		MetricsApplication.logger.info("Parsing id to ObjectId");
		ObjectId idIncoming = new ObjectId(id);

		MetricsApplication.logger.info("Creating list to save filter by id");
		List<MetricsCollection> listMetricsFiltredDates = new ArrayList<MetricsCollection>();

		MetricsApplication.logger.info("the liat have " + metrics.size() + " elements");
		MetricsApplication.logger.info("Comparing id in list whit value id provided by user");
		// 0 = evaluator_id
		// 1 = evaluated_id
		// 2 = sprint_id
		switch (typeId) {
		case 0: {
			MetricsApplication.logger.info("Comparing evaluator_id in list whit value id provided by user");
			for (MetricsCollection metric : metrics) {
				MetricsApplication.logger.info("Parsing Evaluator_id to ObjectId");
				ObjectId idDB = new ObjectId(metric.getEvaluator_id());
				if (idDB.compareTo(idIncoming) == 0) {

					listMetricsFiltredDates.add(metric);
				}
			}
			if (listMetricsFiltredDates.size() == 0)
				throw new ResponseStatusException(HttpStatus.NOT_FOUND,
						"were not found with the getEvaluator_id specified");
			break;
		}
		case 1: {
			MetricsApplication.logger.info("Comparing evaluated_id in list whit value id provided by user");
			for (MetricsCollection metric : metrics) {
				MetricsApplication.logger.info("Parsing getEvaluated_id to ObjectId");
				ObjectId idDB = new ObjectId(metric.getEvaluated_id());
				if (idDB.compareTo(idIncoming) == 0) {

					listMetricsFiltredDates.add(metric);
				}
			}
			if (listMetricsFiltredDates.size() == 0)
				throw new ResponseStatusException(HttpStatus.NOT_FOUND,
						"were not found with the evaluated_id specified");
			break;
		}
		case 2: {
			MetricsApplication.logger.info("Comparing sprint_id in list whit value id provided by user");
			for (MetricsCollection metric : metrics) {
				MetricsApplication.logger.info("Parsing getSprint_id to ObjectId");
				ObjectId idDB = new ObjectId(metric.getSprint_id());
				MetricsApplication.logger
						.info("Compare " + idDB + " with " + idIncoming + " queals " + idDB.compareTo(idIncoming));
				if (idDB.compareTo(idIncoming) == 0) {
					MetricsApplication.logger.info("Adding record getSprint_id to ObjectId");
					listMetricsFiltredDates.add(metric);
				}
			}
			if (listMetricsFiltredDates.size() == 0)
				throw new ResponseStatusException(HttpStatus.NOT_FOUND, "were not found with the sprint_id specified");
			break;
		}

		}
		MetricsApplication.logger.info(listMetricsFiltredDates.size());
		if (orderBy == 0) {
			MetricsApplication.logger
					.info("Applying filter selected to " + listMetricsFiltredDates.size() + " elements");
			listMetricsFiltredDates = Functions.OrderByAscending(listMetricsFiltredDates);
		} else if (orderBy == 1) {
			MetricsApplication.logger
					.info("Applying filter selected to " + listMetricsFiltredDates.size() + " elements");
			listMetricsFiltredDates = Functions.OrderByDescending(listMetricsFiltredDates);
		}
		MetricsApplication.logger
				.info("Return new list with the metric matches with " + listMetricsFiltredDates.size() + " elements");
		return listMetricsFiltredDates;
	}
}
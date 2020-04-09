package com.metrics.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

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
			throw new HttpClientErrorException(HttpStatus.NOT_FOUND, HttpExceptions.findById404);
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
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, HttpExceptions.deleteMetric404);
		}
	}

	@Override
	public MetricsCollection updateMetric(CreateMetricRequest request, String id) {
		if (!repository.existsById(id)) {
			MetricsApplication.logger.error("Tried to update metric but couldnt find the ID given");
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, HttpExceptions.updateMetric404);
		}
		
		MetricsApplication.logger.info("Creating metric object");
		MetricsCollection metric = new MetricsCollection();
		MetricsApplication.logger.info("calling data validation method");
		metric = Functions.CreateMetricRequestToMetricsCollection(Functions.testMetricIntegrity(request, 1));
			MetricsApplication.logger.info("data validation test passed, saving new object");
			metric = orikaMapperFacade.map(request, MetricsCollection.class);
			MetricsApplication.logger.info("object created succesfully");
			metric.setId(id);
		MetricsApplication.logger
				.info("Object created and validated successfully, saving into the database and returning the object");
		return repository.save(metric);
	}

	@Override
	public List<MetricsCollection> getAllMetricsPaginated(int page, int size, List<MetricsCollection> metrics) {
		List<MetricsCollection> listMetricsFiltredDates = new ArrayList<MetricsCollection>();
		
		if (size <= 0) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, HttpExceptions.getMetricsPaginationSize400);
		}
		if (page <= 0) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, HttpExceptions.getMetricsPaginationPage400);
		}
		MetricsApplication.logger.info("Starting variables with size per page and number of pages " + page + " and size " + size);
		MetricsApplication.logger.info("size "	+ size + " metric size " + metrics.size());
		if (page == 1 && size > metrics.size() && metrics.size() == 1) {
			size = 1;
		}else if (page == 1 && size > metrics.size() && metrics.size() > 1) {
			size = metrics.size();
		}else if(page == 1 && size==1) {
			size = metrics.size();
		}
		int pages = metrics.size() / size;
		if( metrics.size() / size == 1) {
			pages++;
		}
		int lastElements =  metrics.size() % size;
		MetricsApplication.logger.info("Starting size in " + size);
		
		if (pages < page) {
			MetricsApplication.logger.info(
					"Return empty list");
			return listMetricsFiltredDates;
		}
		
		MetricsApplication.logger.info("Starting variables with size per page and number of pages " + pages);
		
		if (page == pages && size > lastElements) {
			size = lastElements;
			MetricsApplication.logger.info(
					"The elements is out range of numbers of elements in List and assigning the last numbers elements to size "
							+ size);
		}
		

		int index = 0;
		if (page != 0) {

			index = (page - 1) * size;
		}
		if (metrics == null || metrics.size() < index) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
					HttpExceptions.getMetricsPaginationRange400);
		}
		MetricsApplication.logger.info(
				"index " + index + " final " + Math.min(index + size, metrics.size()));
		listMetricsFiltredDates = metrics.subList(index, Math.min(index + size, metrics.size()));

		
		MetricsApplication.logger.info("Return the page and size elements per page");
		return listMetricsFiltredDates;
	}

	@Override
	public List<MetricsCollection> getItemsFromDateRange(Date startDate, Date endDate,
			List<MetricsCollection> metrics) {

		MetricsApplication.logger.info("Creating list to save filter by range date");
		List<MetricsCollection> listMetricsFiltredDates = new ArrayList<MetricsCollection>();
		MetricsApplication.logger.info("Getting range dates");
		for (MetricsCollection metric : metrics) {
			try {

				if ((Functions.stringToDate(metric.getDate()).after(startDate)
						|| Functions.stringToDate(metric.getDate()).equals(startDate))
						&& Functions.stringToDate(metric.getDate()).before(endDate)
						|| Functions.stringToDate(metric.getDate()).equals(endDate)) {
					listMetricsFiltredDates.add(metric);
				}

			} catch (Exception e) {
			}

		}
		// 0 = Filter by id
		// 1 = evaluator_id
		// 2 = evaluated_id
		// 3 = date;
		// 4 = sprint_id;
		

		MetricsApplication.logger
				.info("Return new list with the metric matches with " + listMetricsFiltredDates.size() + " elements");
		return listMetricsFiltredDates;
	}

	@Override
	public List<MetricsCollection> getItemsFromIdFilter(String id, List<MetricsCollection> metrics, int typeId) {

		MetricsApplication.logger.info("Parsing id to ObjectId");
		// ObjectId idIncoming = new ObjectId(id);

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
				// ObjectId idDB = new ObjectId(metric.getEvaluator_id());
				if (metric.getEvaluator_id() != null)
					if (metric.getEvaluator_id().compareTo(id) == 0) {

						listMetricsFiltredDates.add(metric);
					}
			}
			if (listMetricsFiltredDates.size() == 0)
				throw new ResponseStatusException(HttpStatus.NOT_FOUND,
						HttpExceptions.evaluatorId404);
			break;
		}
		case 1: {
			MetricsApplication.logger.info("Comparing evaluated_id in list whit value id provided by user " + id);
			for (MetricsCollection metric : metrics) {
				// ObjectId idDB = new ObjectId(metric.getEvaluated_id());
				if (metric.getEvaluated_id() != null)
					MetricsApplication.logger.info("Comparing " + metric.getEvaluated_id() + " with " + id);
					if (metric.getEvaluated_id().compareTo(id) == 0) {

						listMetricsFiltredDates.add(metric);
					}
			}
			if (listMetricsFiltredDates.size() == 0)
				throw new ResponseStatusException(HttpStatus.NOT_FOUND,
						HttpExceptions.evaluatedId404);
			break;
		}
		case 2: {
			MetricsApplication.logger.info("Comparing sprint_id in list whit value id provided by user");
			for (MetricsCollection metric : metrics) {
				// ObjectId idDB = new ObjectId(metric.getSprint_id());
				if (metric.getSprint_id() != null)
					if (metric.getSprint_id().compareTo(id) == 0) {
						MetricsApplication.logger.info("Adding record getSprint_id to ObjectId");
						listMetricsFiltredDates.add(metric);
					}
			}
			if (listMetricsFiltredDates.size() == 0)
				throw new ResponseStatusException(HttpStatus.NOT_FOUND, HttpExceptions.sprintId404);
			break;
		}

		}
		MetricsApplication.logger.info(listMetricsFiltredDates.size());
		
		MetricsApplication.logger
				.info("Return new list with the metric matches with " + listMetricsFiltredDates.size() + " elements");
		return listMetricsFiltredDates;
	}
}
package com.metrics.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import com.metrics.model.MetricsCollection;
import com.metrics.repository.MetricRepository;
import com.metrics.service.ErrorHandler.HttpExceptionMessage;
import com.metrics.service.ErrorHandler.PathErrorMessage;
import com.metrics.service.ErrorHandler.TypeError;
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
	
	public List<MetricsCollection> getMetricsFilter(String sprint_id, String evaluator_id,
													String evaluated_id, String startDate,
													String endDate, int page, int size, int order) 
	{
		MetricsApplication.logger.info("Creating list..");
		List<MetricsCollection> metricsCollection = new ArrayList<>();
		MetricsApplication.logger.info("Filling list with info");
		
		if(endDate.equals("1000-01-01")) {
			Date date = new Date();
			endDate= new SimpleDateFormat("yyyy-MM-dd").format(date);
		}
		
		// By default size = 100 and order is descending 
		PageRequest firstPageRequest = PageRequest.of(page, size, Sort.Direction.DESC, "date");
		if(order == 0) {
		firstPageRequest = PageRequest.of(page, size, Sort.Direction.ASC, "date");
		}
		MetricsApplication.logger.info(sprint_id);
		
		//Applying the correct filters
		if(sprint_id.equals("") && evaluator_id.equals("") && evaluated_id.equals("")) {
		MetricsApplication.logger.info("Find all");
		MetricsApplication.logger.info(endDate);
		metricsCollection = repository.findByDate(startDate, endDate, firstPageRequest);
		}
		else if(evaluator_id.equals("") && evaluated_id.equals("")) {
		MetricsApplication.logger.info("Find by sprint_id");
		metricsCollection = repository.findBySprintId(sprint_id, startDate, endDate, firstPageRequest);
		}
		else if(sprint_id.equals("") && evaluated_id.equals("")) {
		MetricsApplication.logger.info("Find by evaluator_id");
		metricsCollection = repository.findByEvaluatorId(evaluator_id, startDate, endDate, firstPageRequest);
		}
		else if(evaluator_id.equals("") && sprint_id.equals("")) {
		MetricsApplication.logger.info("Find by evaluated_id");
		metricsCollection = repository.findByEvaluatedId(evaluated_id, startDate, endDate, firstPageRequest);
		}
		else if(sprint_id.equals("")) {
		metricsCollection = repository.findByEvaluatorIdAndEvaluatedId(evaluator_id, evaluated_id, startDate, endDate, firstPageRequest);
		}
		else if(evaluator_id.equals("")) {
		metricsCollection = repository.findBySprintIdAndEvaluatedId(sprint_id, evaluated_id, startDate, endDate, firstPageRequest);
		}
		else if(evaluated_id.equals("")) {
		metricsCollection = repository.findBySpritIdAndEvaluatorId(sprint_id, evaluator_id, startDate, endDate, firstPageRequest);
		}
		else {
		metricsCollection = repository.findBySprintIdAndEvaluatedIdAndEvaluatorId(sprint_id, evaluated_id, evaluator_id, startDate, endDate, firstPageRequest);
		}
		
	
		MetricsApplication.logger.info("Returning lists");
		return metricsCollection;
}





	@Override
	public Optional<MetricsCollection> findById(String id) {
		if (!repository.existsById(id)) {
			MetricsApplication.logger.error("trying to find a metric but  did not found an ID");
			TypeError.httpErrorMessage(new Exception(), HttpStatus.NOT_FOUND.value(), HttpStatus.NOT_FOUND.name(),
					HttpExceptionMessage.IdNotFound404, "/metric/" + id);
			throw new HttpClientErrorException(HttpStatus.NOT_FOUND);
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
			TypeError.httpErrorMessage(new Exception(), HttpStatus.NOT_FOUND.value(), HttpStatus.NOT_FOUND.name(),
					HttpExceptionMessage.IdNotFound404, "/metric/" + id);
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		}
	}

	@Override
	public MetricsCollection updateMetric(CreateMetricRequest request, String id) {
		if (!repository.existsById(id)) {
			MetricsApplication.logger.error("Tried to update metric but couldnt find the ID given");
			TypeError.httpErrorMessage(new Exception(), HttpStatus.NOT_FOUND.value(), HttpStatus.NOT_FOUND.name(),
					HttpExceptionMessage.IdNotFound404, "/metric/" + id);
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		}

		MetricsApplication.logger.info("Creating metric object");
		MetricsCollection metric = new MetricsCollection();
		MetricsApplication.logger.info("calling data validation method");
		metric = TechnicalValidations
				.CreateMetricRequestToMetricsCollection(BusinessMethods.testMetricIntegrity(request, 1));
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
		MetricsApplication.logger
				.info("Starting variables with size per page and number of pages " + page + " and size " + size);
		MetricsApplication.logger.info("size " + size + " metric size " + metrics.size());

		if (page == 1 && size > metrics.size() && metrics.size() == 1) {
			size = 1;
		} else if (page == 1 && size > metrics.size() && metrics.size() > 1) {
			size = metrics.size();
		}
		int pages = metrics.size() / size;
		if ((metrics.size() / size) == 1) {
			pages++;
		}
		int lastElements = metrics.size() % size;
		MetricsApplication.logger.info("Starting size in " + size);
		MetricsApplication.logger.info("Last elements of list " + lastElements);
		if (lastElements > 0) {
			pages++;
		}

		if (pages < page) {
			MetricsApplication.logger.info("Return empty list");
			return listMetricsFiltredDates;
		}

		int index = 0;
		if (page != 0) {

			index = (page - 1) * size;
		}
		if (metrics == null || metrics.size() < index) {
			TypeError.httpErrorMessage(new Exception(), HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.name(),
					HttpExceptionMessage.PaginationInvalidRange400, PathErrorMessage.pathMetric);
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
		MetricsApplication.logger.info("index " + index + " final " + Math.min(index + size, metrics.size()));
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

				if ((TechnicalValidations.stringToDate(metric.getDate()).after(startDate)
						|| TechnicalValidations.stringToDate(metric.getDate()).equals(startDate))
						&& TechnicalValidations.stringToDate(metric.getDate()).before(endDate)
						|| TechnicalValidations.stringToDate(metric.getDate()).equals(endDate)) {
					listMetricsFiltredDates.add(metric);
				}

			} catch (Exception e) {
			}

		}
		MetricsApplication.logger
				.info("Return new list with the metric matches with " + listMetricsFiltredDates.size() + " elements");
		return listMetricsFiltredDates;
	}

	@Override
	public List<MetricsCollection> getItemsFromIdFilter(String id, List<MetricsCollection> metrics, int typeId,
			int typeRequest) {

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
				if (metric.getEvaluator_id() != null)
					if (metric.getEvaluator_id().compareTo(id) == 0) {
						listMetricsFiltredDates.add(metric);
					}
			}
			if ((listMetricsFiltredDates.size() == 0) && (typeRequest != 2)) {
				TypeError.httpErrorMessage(new Exception(), HttpStatus.CONFLICT.value(), HttpStatus.CONFLICT.name(),
						HttpExceptionMessage.Evaluator_IdNotFound404, "/metric/" + id);
				throw new ResponseStatusException(HttpStatus.CONFLICT);
			}
			break;
		}
		case 1: {
			MetricsApplication.logger.info("Comparing evaluated_id in list whit value id provided by user " + id);
			for (MetricsCollection metric : metrics) {
				if (metric.getEvaluated_id() != null) {
					if (metric.getEvaluated_id().compareTo(id) == 0) {
						listMetricsFiltredDates.add(metric);
					}
				}
			}
			if ((listMetricsFiltredDates.size() == 0) && (typeRequest != 2)) {
				TypeError.httpErrorMessage(new Exception(), HttpStatus.CONFLICT.value(), HttpStatus.CONFLICT.name(),
						HttpExceptionMessage.Evaluated_IdNotFound404, "/metric/" + id);
				throw new ResponseStatusException(HttpStatus.CONFLICT);
			}
			break;
		}
		case 2: {
			MetricsApplication.logger.info("Comparing sprint_id in list whit value id provided by user");
			for (MetricsCollection metric : metrics) {
				if (metric.getSprint_id() != null)
					if (metric.getSprint_id().compareTo(id) == 0) {
						listMetricsFiltredDates.add(metric);
					}
			}

			if ((listMetricsFiltredDates.size() == 0) && (typeRequest != 2)) {
				TypeError.httpErrorMessage(new Exception(), HttpStatus.CONFLICT.value(), HttpStatus.CONFLICT.name(),
						HttpExceptionMessage.Sprint_IdNotFound404, "/metric/" + id);
				throw new ResponseStatusException(HttpStatus.CONFLICT);
			}
			break;
		}

		}
		MetricsApplication.logger.info(listMetricsFiltredDates.size());

		MetricsApplication.logger
				.info("Return new list with the metric matches with " + listMetricsFiltredDates.size() + " elements");

		return listMetricsFiltredDates;
	}
}
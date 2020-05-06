package com.metrics.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
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

	public List<MetricsCollection> getMetricsFilter(String sprint_id, String evaluator_id, String evaluated_id,
			String startDate, String endDate, int page, int size, int order) {
		if (TechnicalValidations.haveOnlyNumbers(evaluator_id) && !evaluator_id.equals("")) {
			MetricsApplication.logger.error("evaluator_id had only numbers");
			TypeError.httpErrorMessage(HttpStatus.NOT_FOUND, new Exception(),
					HttpExceptionMessage.Evaluator_IdNotFound404, PathErrorMessage.pathMetric + "/" + evaluator_id);
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		}
		if (TechnicalValidations.haveOnlyNumbers(evaluated_id) && !evaluated_id.equals("")) {
			MetricsApplication.logger.error("evaluated_id had only numbers");
			TypeError.httpErrorMessage(HttpStatus.NOT_FOUND, new Exception(),
					HttpExceptionMessage.Evaluated_IdNotFound404, PathErrorMessage.pathMetric+ "/" + evaluated_id);
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		}
		if (TechnicalValidations.haveOnlyNumbers(sprint_id) && !sprint_id.equals("")) {
			MetricsApplication.logger.error("sprint_id had only numbers");
			TypeError.httpErrorMessage(HttpStatus.NOT_FOUND, new Exception(),
					HttpExceptionMessage.Sprint_IdNotFound404, PathErrorMessage.pathMetric+ "/" + sprint_id);
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		}
		
		MetricsApplication.logger.info("Creating list..");
		List<MetricsCollection> metricsCollection = new ArrayList<>();
		MetricsApplication.logger.info("Filling list with info");

		if (endDate.equals("1000-01-01")) {
			Date date = new Date();
			endDate = new SimpleDateFormat("yyyy-MM-dd").format(date);
		}

		// By default size = 100 and order is descending
		PageRequest firstPageRequest = PageRequest.of(page, size, Sort.Direction.DESC, "date");
		if (order == 0) {
			firstPageRequest = PageRequest.of(page, size, Sort.Direction.ASC, "date");
		}
		MetricsApplication.logger.info(sprint_id);

		// Applying the correct filters
		if (sprint_id.equals("") && evaluator_id.equals("") && evaluated_id.equals("")) {
			MetricsApplication.logger.info("Find all");
			MetricsApplication.logger.info(endDate);
			metricsCollection = repository.findByDate(startDate, endDate, firstPageRequest);
		} else if (evaluator_id.equals("") && evaluated_id.equals("")) {
			if (TechnicalValidations.VerifyingID(sprint_id)) {
				MetricsApplication.logger.info("Find by sprint_id");
				metricsCollection = repository.findBySprintId(sprint_id, startDate, endDate, firstPageRequest);
			}
		} else if (sprint_id.equals("") && evaluated_id.equals("")) {
			if (TechnicalValidations.VerifyingID(evaluator_id)) {
				MetricsApplication.logger.info("Find by evaluator_id");
				metricsCollection = repository.findByEvaluatorId(evaluator_id, startDate, endDate, firstPageRequest);
			}
		} else if (evaluator_id.equals("") && sprint_id.equals("")) {
			if (TechnicalValidations.VerifyingID(evaluated_id)) {
				MetricsApplication.logger.info("Find by evaluated_id");
				metricsCollection = repository.findByEvaluatedId(evaluated_id, startDate, endDate, firstPageRequest);
			}
		} else if (sprint_id.equals("")) {
			if (TechnicalValidations.VerifyingID(evaluated_id) && TechnicalValidations.VerifyingID(evaluator_id)) {
				metricsCollection = repository.findByEvaluatorIdAndEvaluatedId(evaluator_id, evaluated_id, startDate,
						endDate, firstPageRequest);
			}
		} else if (evaluator_id.equals("")) {
			if (TechnicalValidations.VerifyingID(sprint_id) && TechnicalValidations.VerifyingID(evaluated_id)) {
				metricsCollection = repository.findBySprintIdAndEvaluatedId(sprint_id, evaluated_id, startDate, endDate,
						firstPageRequest);
			}
		} else if (evaluated_id.equals("")) {
			if (TechnicalValidations.VerifyingID(sprint_id) && TechnicalValidations.VerifyingID(evaluator_id)) {
				metricsCollection = repository.findBySpritIdAndEvaluatorId(sprint_id, evaluator_id, startDate, endDate,
						firstPageRequest);
			}
		} else {
			if (TechnicalValidations.VerifyingID(sprint_id) && TechnicalValidations.VerifyingID(evaluator_id)
					&& TechnicalValidations.VerifyingID(evaluated_id)) {
				metricsCollection = repository.findBySprintIdAndEvaluatedIdAndEvaluatorId(sprint_id, evaluated_id,
						evaluator_id, startDate, endDate, firstPageRequest);
			}
		}

		MetricsApplication.logger.info("Returning lists");
		return metricsCollection;
	}

	@Override
	public MetricsCollection findById(String id) {
		MetricsCollection metricsCollection = new MetricsCollection();
		if (!repository.existsById(id)) {
			MetricsApplication.logger.error("trying to find a metric but  did not found an ID");
			TypeError.httpErrorMessage(HttpStatus.NOT_FOUND, new Exception(), HttpExceptionMessage.IdNotFound404,
					"/metric/" + id);
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		}else if(repository.findById(id).isPresent()){
			metricsCollection = repository.findById(id).get();
		}
		MetricsApplication.logger.info("Returning metric");
		return metricsCollection;

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
			TypeError.httpErrorMessage(HttpStatus.NOT_FOUND, new Exception(), HttpExceptionMessage.IdNotFound404,
					"/metric/" + id);
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		}
	}

	@Override
	public MetricsCollection updateMetric(CreateMetricRequest request, String id) {
		
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
}
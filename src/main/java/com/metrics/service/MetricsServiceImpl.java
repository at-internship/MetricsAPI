package com.metrics.service;

import java.util.ArrayList;
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
public class MetricsServiceImpl implements MetricsService
{
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
		if (!repository.existsById(id))
		{
			MetricsApplication.logger.error("trying to find a metric but  did not found an ID");
			throw new HttpClientErrorException(HttpStatus.NOT_FOUND, "No metric found with the given id");	
		}
		MetricsApplication.logger.info("Returning metric");
		return repository.findById(id);
    }
    
    	@Override
	public MetricsCollection newMetric(CreateMetricRequest request)
	{
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
		 }
		 else {
			 MetricsApplication.logger.error("tried to delete metric but couldnt find an ID");
			 throw new ResponseStatusException(HttpStatus.NOT_FOUND, "The given ID does not exist");
		 }
	 }
   
   	@Override
	public MetricsCollection updateMetric(CreateMetricRequest request, String id) {
		if (!repository.existsById(id)) {
			MetricsApplication.logger.error("Tried to update metric but couldnt find the ID given");
			throw new ResponseStatusException(
			          HttpStatus.NOT_FOUND, "Metric not found");
		}
		MetricsApplication.logger.info("Creating metric object");
		MetricsCollection metric = new MetricsCollection();
		MetricsApplication.logger.info("calling data validation method");
		MappingTest test = new MappingTest();
		if(test.MappingTestMetric(request)) {
			MetricsApplication.logger.info("data validation test passed, saving new object");
			metric = orikaMapperFacade.map(request, MetricsCollection.class);
			MetricsApplication.logger.info("object created succesfully");
			metric.setId(id);
		}else {
			MetricsApplication.logger.error("Tried to create the object but didnt pass the valiation method");
			throw new ResponseStatusException(
			          HttpStatus.BAD_REQUEST);
		}
		MetricsApplication.logger.info("Object created and validated successfully, saving into the database and returning the object");
		return repository.save(metric);
	}
}
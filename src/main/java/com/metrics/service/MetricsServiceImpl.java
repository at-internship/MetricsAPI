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
		List<MetricsCollection> metricsCollection = new ArrayList<>();
		metricsCollection = repository.findAll();
		return metricsCollection;
	}
	
	@Override
	public Optional<MetricsCollection> findById(String id) {
		if (!repository.existsById(id))
			throw new HttpClientErrorException(HttpStatus.NOT_FOUND, "No metric found with the given id");	
		return repository.findById(id);
    }
    
    	@Override
	public MetricsCollection newMetric(CreateMetricRequest request)
	{
			MetricsCollection metric = new MetricsCollection();
			metric = orikaMapperFacade.map(request, MetricsCollection.class);
			repository.save(metric);
			return metric;
	}
	
	 public void deleteMetric(String id) {
		 if (repository.existsById(id)) {
			 repository.deleteById(id);
		 }
		 else {
			 throw new ResponseStatusException(HttpStatus.NOT_FOUND, "The given ID does not exist");
		 }
	 }
   
   	@Override
	public MetricsCollection updateMetric(CreateMetricRequest request, String id) {
		if (!repository.existsById(id))
			throw new ResponseStatusException(
			          HttpStatus.NOT_FOUND, "Metric not found");
		MetricsCollection metric = new MetricsCollection();
		MappingTest test = new MappingTest();
		if(test.MappingTestMetric(request)) {
			
			metric = orikaMapperFacade.map(request, MetricsCollection.class);
			metric.setId(id);
		}else {
			throw new ResponseStatusException(
			          HttpStatus.BAD_REQUEST);
		}
		
		return repository.save(metric);
	}
}
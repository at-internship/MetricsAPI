package com.metrics.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import com.metrics.model.MetricsCollection;
import com.metrics.model.metrics;
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
	public List<MetricsCollection> getAllMetricsPaginated(int start, int size, List<MetricsCollection> metrics, int orderBy) {
		List<MetricsCollection> listMetrics = metrics;
		listMetrics = Functions.OrderByAscending(listMetrics, orderBy);
		  return listMetrics.subList(start, start + size);
	}
	
	@Override
	public List<MetricsCollection> getItemsFromDateRange(Date startDate, Date endDate, List<MetricsCollection> metrics, int orderBy) {
		List<MetricsCollection> listMetricsFiltredDates = new ArrayList<MetricsCollection>();
		for (MetricsCollection metric: metrics) {
			
			if (Functions.stringToDate(metric.getDate()).after(startDate) && 
			    Functions.stringToDate(metric.getDate()).before(endDate)) {
				listMetricsFiltredDates.add(metric);
			}
		}
		// 0 = Filter by id
		// 1 = evaluator_id
		// 2 = evaluated_id
		// 3 = date;
		// 4 = sprint_id;
		listMetricsFiltredDates = Functions.OrderByAscending(listMetricsFiltredDates, orderBy);
		return listMetricsFiltredDates;
	}
	
	@Override
	public List<MetricsCollection> getSpecificItemsFromTypeIdRange(String startId, String endId, List<MetricsCollection> metrics, int filtredBy, int orderBy) {
		List<MetricsCollection> listMetricsFiltredDates = new ArrayList<MetricsCollection>();
		ObjectId startIdLocal = new ObjectId(startId);
		ObjectId endtIdLocal = new ObjectId(endId);
		
		// 0 = Filter by id
		// 1 = Filter by evaluator_id
		// 2 = Filter by evaluated_id
		// 3 = Filter by sprint_id;
			switch(filtredBy) {
				case 0:{
					System.out.println("Applying order by id");
					for (MetricsCollection metric: metrics) {
						ObjectId idToComparate = new ObjectId(metric.getId());
						if (startIdLocal.compareTo(idToComparate) <= 0 && endtIdLocal.compareTo(idToComparate) >= 0) {
							listMetricsFiltredDates.add(metric);
						}
					}
					
					break;
				}
				case 1:{
					System.out.println("Applying order by Evaluator_id");
					for (MetricsCollection metric: metrics) {
						ObjectId idToComparate = new ObjectId(metric.getEvaluator_id());
						if (startIdLocal.compareTo(idToComparate) <= 0 && endtIdLocal.compareTo(idToComparate) >= 0) {
							listMetricsFiltredDates.add(metric);
						}
					}
					
					break;
				}
				case 2:{
					System.out.println("Applying order by Evaluated_id");
					for (MetricsCollection metric: metrics) {
						ObjectId idToComparate = new ObjectId(metric.getEvaluated_id());
						if (startIdLocal.compareTo(idToComparate) <= 0 && endtIdLocal.compareTo(idToComparate) >= 0) {
							listMetricsFiltredDates.add(metric);
						}
					}
					
					break;
				}
				case 3:{
					System.out.println("Applying order by Sprint_id");
					for (MetricsCollection metric: metrics) {
						ObjectId idToComparate = new ObjectId(metric.getSprint_id());
						if (startIdLocal.compareTo(idToComparate) <= 0 && endtIdLocal.compareTo(idToComparate) >= 0) {
							listMetricsFiltredDates.add(metric);
						}
					}
					
					break;
				}
			}
			listMetricsFiltredDates = Functions.OrderByAscending(listMetricsFiltredDates, orderBy);
			System.out.println(listMetricsFiltredDates.size());
			return listMetricsFiltredDates;
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
   		
   		MetricsCollection metric = new MetricsCollection();
   		MappingTest test = new MappingTest();
   		
		if (!repository.existsById(id))
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Metric not found");
		
		if(test.MappingTestMetric(request)) {
			metric = orikaMapperFacade.map(request, MetricsCollection.class);
			Functions.VerifyingUUID(id);
			metric.setId(id);
		}else {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
		
		return repository.save(metric);
	}
}
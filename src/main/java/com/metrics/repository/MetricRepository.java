package com.metrics.repository;

import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.metrics.model.MetricsCollection;

public interface MetricRepository extends MongoRepository<MetricsCollection, String> {
	List<MetricsCollection> findAll();
	MetricsCollection findById();
	
	 @Query("{ 'date' : { $gte: ?0, $lte: ?1 } }")
	 List<MetricsCollection> findByDate(String startDate, String endDate, PageRequest firstPageRequest);
	
	 @Query("{evaluator_id:'?0', 'date' : { $gte: ?1, $lte: ?2 }}")
	 List<MetricsCollection> findByEvaluatorId(String evaluator_id, String startDate, String endDate, PageRequest firstPageRequest);
	 
	 @Query("{evaluated_id:'?0', 'date' : { $gte: ?1, $lte: ?2 }}")
	 List<MetricsCollection> findByEvaluatedId(String evaluated_id, String startDate, String endDate, PageRequest firstPageRequest);
	 
	 @Query("{sprint_id:'?0', 'date' : { $gte: ?1, $lte: ?2 }}")
	 List<MetricsCollection> findBySprintId(String sprint_id, String startDate, String endDate, PageRequest firstPageRequest);
	 
	 @Query("{evaluator_id:'?0', evaluated_id:'?1', 'date' : { $gte: ?2, $lte: ?3 }}")
	 List<MetricsCollection> findByEvaluatorIdAndEvaluatedId(String evaluator_id, String evaluated_id, String startDate, String endDate, PageRequest firstPageRequest);
	 
	 @Query("{sprint_id:'?0', evaluator_id:'?1', 'date' : { $gte: ?2, $lte: ?3 }}")
	 List<MetricsCollection> findBySpritIdAndEvaluatorId(String sprint_id, String evaluator_id, String startDate, String endDate, PageRequest firstPageRequest);
	 
	 @Query("{sprint_id:'?0', evaluated_id:'?1', 'date' : { $gte: ?2, $lte: ?3 }}")
	 List<MetricsCollection> findBySprintIdAndEvaluatedId(String sprint_id, String evaluated_id, String startDate, String endDate, PageRequest firstPageRequest);
	 
	 @Query("{sprint_id:'?0', evaluated_id:'?1', evaluator_id:'?2', 'date' : { $gte: ?3, $lte: ?4 }}")
	 List<MetricsCollection> findBySprintIdAndEvaluatedIdAndEvaluatorId(String sprint_id, String evaluated_id, String evaluator_id, String startDate, String endDate, PageRequest firstPageRequest);
}
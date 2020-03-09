package com.metrics.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.metrics.model.MetricsCollection;

public interface MetricRepository extends MongoRepository<MetricsCollection, String> {
	List<MetricsCollection> findAll();
	Optional<MetricsCollection> findById(String id);

}

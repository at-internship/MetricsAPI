package com.metrics.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import com.metrics.model.MetricsCollection;
@Repository
public interface MetricRepository extends MongoRepository<MetricsCollection, String>{
}

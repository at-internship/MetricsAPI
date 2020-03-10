package com.metrics.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.metrics.repository.MetricRepository;

@Service
public class MongoBasicServiceImpl implements MongoBasicService {
	private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(MongoBasicServiceImpl.class);
	
	@Autowired
	MetricRepository repository;

	
	 @Override
	 public void deleteMetric(String id) {
		 if (repository.existsById(id)) {
			 log.debug("Deleting user with id: " + id);
			 repository.deleteById(id);
		 }
		 else {
			 log.error("No user was found for the given id.");
		 }
	 }
}

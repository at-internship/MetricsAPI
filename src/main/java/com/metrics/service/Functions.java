package com.metrics.service;

import java.sql.Timestamp;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.metrics.MetricsApplication;
import com.metrics.domain.CreateMetricRequest;
import com.metrics.model.MetricsCollection;
import com.metrics.model.blockers;
import com.metrics.model.metrics;
import com.metrics.model.proactive;
import com.metrics.model.retroactive;

public class Functions {
	public static CreateMetricRequest MetricsCollectionToCreateMetricRequest(MetricsCollection metric) {
		CreateMetricRequest listIncoming = new CreateMetricRequest(metric.getId(), metric.getEvaluator_id(),
				metric.getEvaluated_id(), metric.getType(), metric.getDate(), metric.getSprint_id(),
				metric.getMetrics());
		return listIncoming;
	}

	public static void VerifyingUUID(String uuid)  {
		try {
			MetricsApplication.logger.info(uuid);
			if (uuid.length() == 24) {
				ObjectId.isValid(uuid);
			}else{
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The UUID has incorrect Format");
			}
				
		} catch (IllegalArgumentException error) {
			
		}
	}

	public static void IsDBEmpty(List<MetricsCollection> metrics) {
		MetricsApplication.logger.info("The method found " + metrics.size() + " records");
		if (metrics.size() == 0)
			throw new ResponseStatusException(HttpStatus.NO_CONTENT, "The DB have not records");
	}

	public static void VerifyingAllTypesDatasIntoDB(List<MetricsCollection> metrics) {
		MappingTest testingList = new MappingTest();
		MetricsApplication.logger.info("Verifying records and validating type data");
		for (MetricsCollection metric : metrics) {
			if (!testingList.MappingTestMetric(Functions.MetricsCollectionToCreateMetricRequest(metric)))
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid structure metric " + metric.getId());
		}
	}

	public static boolean VerifyingTimeStampValid(String inputString) {
		SimpleDateFormat format = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSSSS");
		try {
			format.parse(inputString);
			return true;
		} catch (ParseException e) {
			return false;
		}
	}

	public static Timestamp stringToTimestamp(String date) throws ParseException {

		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		Timestamp ts = new Timestamp(((java.util.Date) df.parse(date)).getTime());

		return ts;
	}

	public static String mapToJson(Object obj) throws JsonProcessingException {
		ObjectMapper objectMapper = new ObjectMapper();
		return objectMapper.writeValueAsString(obj);
	}

	public static <T> T mapFromJson(String json, Class<T> clazz)
			throws JsonParseException, JsonMappingException, IOException {

		ObjectMapper objectMapper = new ObjectMapper();
		return objectMapper.readValue(json, clazz);
	}

	public static CreateMetricRequest SetDefaultDataEmptyField(CreateMetricRequest metric) {
		CreateMetricRequest collection = metric;

		if (collection.getMetrics() == null) {
			collection.setMetrics(new metrics(false, false, new blockers(false, "Empty"),
					new proactive(false, false, false, false), new retroactive(false, "Empty")));
		}

		if (collection.getMetrics().getBlockers() == null) {
			collection.getMetrics().setBlockers(new blockers(false, "Empty"));
		}

		if (collection.getMetrics().getProactive() == null) {
			collection.getMetrics().setProactive(new proactive(false, false, false, false));
		}

		if (collection.getMetrics().getRetroactive() == null) {
			collection.getMetrics().setRetroactive(new retroactive(false, "Empty"));
		}

		if (collection.getDate().isEmpty()) {
			collection.setDate("1000-01-01");
		}

		if (collection.getType().isEmpty()) {
			collection.setDate("Empty");
		}
		if (collection.getEvaluated_id().isEmpty()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}

		if (collection.getEvaluator_id().isEmpty()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
		if (collection.getSprint_id().isEmpty()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}

		if (collection.getMetrics().getBlockers().getComments().isEmpty()) {
			collection.getMetrics().getBlockers().setComments("Empty");
		}

		if (collection.getMetrics().getRetroactive().getComments().isEmpty()) {
			collection.getMetrics().getRetroactive().setComments("Empty");
		}

		return collection;
	}

	public static List<MetricsCollection> OrderByAscending(List<MetricsCollection> listMetric) {
		List<MetricsCollection> listOrder = listMetric;
		Collections.sort(listOrder, new Comparator<MetricsCollection>() {
			@Override
			public int compare(MetricsCollection o1, MetricsCollection o2) {
				return o1.getDate().compareTo(o2.getDate());
			}
		});
		return listOrder;
	}

	public static List<MetricsCollection> OrderByDescending(List<MetricsCollection> listMetric) {
		List<MetricsCollection> listOrder = listMetric;
		Collections.sort(listOrder, new Comparator<MetricsCollection>() {

			@Override
			public int compare(MetricsCollection o1, MetricsCollection o2) {
				return o2.getDate().compareTo(o1.getDate());
			}

		});
		return listOrder;
	}
}

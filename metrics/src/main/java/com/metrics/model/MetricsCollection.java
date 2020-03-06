package com.metrics.model;

import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import javax.persistence.*;

import org.springframework.data.mongodb.core.mapping.Document;
//TestMerge
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Document(collection = "Metrics Collection")
public class MetricsCollection {
	@Id
	private long id;
	private long evaluator_id;
	private long evaluated_id;
	private String type;
	private Timestamp date;
	private long sprint_id;
	private metrics metrics;
	
	public MetricsCollection(String type, 
							 Timestamp date,
							 com.metrics.model.metrics metrics) {
		this.type = type;
		this.date = date;
		this.metrics = metrics;
	}
}

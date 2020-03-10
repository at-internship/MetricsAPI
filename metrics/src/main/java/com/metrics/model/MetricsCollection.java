package com.metrics.model;

import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import javax.persistence.*;

import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Document(collection = "metrics")
public class MetricsCollection {
	@Id
	private String id;
	private String evaluator_id;
	private String evaluated_id;
	private String type;
	private String date;
	private String sprint_id;
	private metrics metrics;
	
	public MetricsCollection(String type, 
							 String date,
							 com.metrics.model.metrics metrics) {
		this.type = type;
		this.date = date;
		this.metrics = metrics;
	}
}

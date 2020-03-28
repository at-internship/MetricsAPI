package com.metrics.model;

import javax.persistence.Entity;
import javax.persistence.Id;

import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Document(collection = "sprints")
public class SprintsCollection {
	@Id
	private String id;
	private String name;
	private String technology;
	private Boolean active;
	private Boolean is_backlog;
	private String start_date;
	private String end_date;
	
}

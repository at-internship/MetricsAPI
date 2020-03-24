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
@Document(collection = "users")
public class UsersCollection {
	@Id
	private String userId;
	private String firstName;
	private String lastName;
	private String email;
	private String phone;
	private String startDate;
	private String endDate;
	private String activeTechnology;
	private String status;
	private String role;
	
}

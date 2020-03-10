package com.metrics.model;

import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
//this is a commit of Irvin
public class blockers {

	private boolean blocked;
	private String comments;
}

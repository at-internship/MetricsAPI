package com.metrics.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@AllArgsConstructor
public class ErrorMessage {

	private String timestamp;
	private int status;
	private String error;
	private String message;
	private String path;

}

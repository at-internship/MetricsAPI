package com.metrics.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import springfox.documentation.annotations.ApiIgnore;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiIgnore
public class blockersString {

	private String blocked;
	private String comments;
}
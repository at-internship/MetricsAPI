package com.metrics.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import springfox.documentation.annotations.ApiIgnore;


@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiIgnore
public class proactiveString {
	private String looked_for_help;
	private String provided_help;
	private String worked_ahead;
	private String shared_resources;
}
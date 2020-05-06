package com.metrics.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import springfox.documentation.annotations.ApiIgnore;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ApiIgnore
public class proactiveString {
	private String looked_for_help;
	private String provided_help;
	private String worked_ahead;
	private String shared_resources;
}
package com.metrics.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import springfox.documentation.annotations.ApiIgnore;

@NoArgsConstructor
@AllArgsConstructor
@Data
@ApiIgnore
public class retroactiveString {
	private String delayed_looking_help;
	private String comments;
}
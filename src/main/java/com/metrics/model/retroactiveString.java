package com.metrics.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import springfox.documentation.annotations.ApiIgnore;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ApiIgnore
public class retroactiveString {
	private String delayed_looking_help;
	private String comments;
}
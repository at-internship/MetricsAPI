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
public class blockersString {

	private String blocked;
	private String comments;
}
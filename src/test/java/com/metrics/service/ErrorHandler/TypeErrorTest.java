package com.metrics.service.ErrorHandler;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.metrics.service.ClientValidations;

class TypeErrorTest {

	@Test
	public void TypeError() {
		TypeError typeError = new TypeError();
		assertNotNull(typeError);
	}

}

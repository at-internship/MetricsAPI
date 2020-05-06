package com.metrics.service;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class ClientValidationsTest {

	@Test
	public void ClientValidations() {
		ClientValidations clientValidations = new ClientValidations();
		assertNotNull(clientValidations);
	}

}

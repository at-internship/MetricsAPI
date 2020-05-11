package com.metrics.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.springframework.web.server.ResponseStatusException;

import com.metrics.service.StaticFunctionsVariables.StaticVariables;

public class VerifyingDate {
	
	public static void VerifyingDateValidTest() {
		assertEquals("2020-09-22", BusinessMethods.VerifyingDateValid("2020-09-22", 1, StaticVariables.id));
	}
	
	
	public static void VerifyingDateOnlyNumbersTest() {
		assertEquals("1000-01-01", BusinessMethods.VerifyingDateValid("2020321", 1, StaticVariables.id));
	}
	
	
	public static void VerifyingDateOnlyLettersTest() {
		assertEquals("1000-01-01", BusinessMethods.VerifyingDateValid("zfcsddsfsd", 1, StaticVariables.id));
	}
	
	
	public static void VerifyingDateIncompletElementsTest() {
		assertEquals("1000-01-01", BusinessMethods.VerifyingDateValid("1000-01", 2, StaticVariables.id));
	}
	
	
	public static void VerifyingDateManytElementsTest() {
		assertEquals("1000-01-01", BusinessMethods.VerifyingDateValid("1000-01-12-12", 2, StaticVariables.id));
	}
	
	
	public static void VerifyingDatePutOrPostTest() {
		assertThrows(ResponseStatusException.class,() -> BusinessMethods.VerifyingDateValid("202-09-22", 1, StaticVariables.id));
	}
	
	
	public static void VerifyingDateBadOrderTest() {
		assertEquals("2020-12-09", BusinessMethods.VerifyingDateValid("09-12-2020", 2, StaticVariables.id));
	}
	
	
	public static void VerifyingDateBadYearTest() {
		assertThrows(ResponseStatusException.class,() -> BusinessMethods.VerifyingDateValid("202-09-22", 2, StaticVariables.id));
	}
	
	
	public static void VerifyingDateBadYearWrongOrderTest() {
		assertThrows(ResponseStatusException.class,() -> BusinessMethods.VerifyingDateValid("02-09-223", 2, StaticVariables.id));
	}
	
	
	public static void VerifyingDateBadDayTest() {
		assertThrows(ResponseStatusException.class,() -> BusinessMethods.VerifyingDateValid("2020-09-2", 2, StaticVariables.id));
	}
	
	
	public static void VerifyingDateBadMonthTest() {
		assertThrows(ResponseStatusException.class,() -> BusinessMethods.VerifyingDateValid("2020-9-22", 2, StaticVariables.id));
	}
	
	
	public static void VerifyingDateExceptionParseTest() {
		assertThrows(ResponseStatusException.class,() -> BusinessMethods.VerifyingDateValid("y324-23-12", 0, StaticVariables.id));
	}
	
	
	public static void VerifyingDateOutRangeTest() {
		assertThrows(ResponseStatusException.class,() -> BusinessMethods.VerifyingDateValid("2324-23-12", 1, StaticVariables.id));
	}
	
	
	public static void VerifyingDateOutRangeDayYearLeapTest() {
		assertThrows(ResponseStatusException.class,() -> BusinessMethods.VerifyingDateValid("2020-02-30", 2, StaticVariables.id));
	}
	
	
	public static void VerifyingDateOutRangeDayYearNotLeapTest() {
		assertThrows(ResponseStatusException.class,() -> BusinessMethods.VerifyingDateValid("2021-02-29", 2, StaticVariables.id));
	}
	
	public static void VerifyingDateInvalidDayTest() {
		assertThrows(ResponseStatusException.class,() -> BusinessMethods.VerifyingDateValid("2021-03-32", 2, StaticVariables.id));
	}
}

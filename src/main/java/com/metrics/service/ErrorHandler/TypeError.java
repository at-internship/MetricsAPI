package com.metrics.service.ErrorHandler;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ResponseStatusException;

import com.metrics.model.ErrorMessage;
import com.metrics.service.BusinessMethods;
import com.metrics.service.TechnicalValidations;

public class TypeError {
	static HttpStatus newTypeError = HttpStatus.BAD_REQUEST;
	static int httpError;
	static String httpNameError;
	static String message;
	static String path;
	
	public static void httpErrorMessage(HttpStatus typeError, Exception error, String messageParameter, String pathParameter) {
		ExceptionAnyHandler manejador = new ExceptionAnyHandler();
		CleanMessage();
		newTypeError = typeError;
		httpError = typeError.value();
		httpNameError = typeError.name();
		httpNameError = httpNameError.replaceAll("_", " ");
		httpNameError = httpNameError.toLowerCase();
		httpNameError = BusinessMethods.capitalizeWord(httpNameError);
		message = messageParameter;
		path = pathParameter;
		manejador.handleAnyException(error);
		
	}
	public static void CleanMessage() {
		TypeError.httpError = 0;
		TypeError.httpNameError = null;
		TypeError.message = null;
		TypeError.path = null;
	}

}

@ControllerAdvice class ExceptionAnyHandler {

	@ExceptionHandler(value = {ResponseStatusException.class})
	public ResponseEntity<Object> handleAnyException(Exception error){
		
		Date actualDate = new Date();
		String strDateFormat = "yyyy-dd-MM HH:mm:ss";
		SimpleDateFormat objSDF = new SimpleDateFormat(strDateFormat);
		
		ErrorMessage errorMessage = new ErrorMessage(objSDF.format(actualDate).toString(),TypeError.httpError, TypeError.httpNameError, TypeError.message, 
				TypeError.path);
		
		return new ResponseEntity<>(errorMessage, new HttpHeaders(),TypeError.newTypeError);
	}
	
	
}

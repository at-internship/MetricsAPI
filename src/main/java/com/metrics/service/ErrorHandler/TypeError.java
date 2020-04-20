package com.metrics.service.ErrorHandler;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.metrics.model.ErrorMessage;

public class TypeError {
	static int httpError;
	static String httpNameError;
	static String message;
	static String path;
	
	public static void httpErrorMessage(Exception error, int httpErrorParameter, String httpNameErrorParameter, String messageParameter, String pathParameter) {
		ExceptionAnyHandler manejador = new ExceptionAnyHandler();
		CleanMessage();
		httpError = httpErrorParameter;
		httpNameError = httpNameErrorParameter;
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

	@ExceptionHandler(value = {Exception.class})
	public ResponseEntity<Object> handleAnyException(Exception error){
		
		Date actualDate = new Date();
		String strDateFormat = "yyyy-dd-MM HH:mm:ss";
		SimpleDateFormat objSDF = new SimpleDateFormat(strDateFormat);
		
		ErrorMessage errorMessage = new ErrorMessage(objSDF.format(actualDate).toString(),TypeError.httpError, TypeError.httpNameError, TypeError.message, 
				TypeError.path);
		
		return new ResponseEntity<>(errorMessage, new HttpHeaders(),HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	
}

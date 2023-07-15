package com.Xogito.Assignment.Utilities;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ResponseStatusException;

import jakarta.validation.ConstraintViolationException;

@ControllerAdvice
public class ResponseHandler {

	/**
	 * Generates an HTTP response body with a message.
	 * 
	 * @param message A String describing the result of an operation.
	 * @param status  The HTTP's response status code.
	 * @return A JSON formatted response with the given message.
	 */
	public static ResponseEntity<?> handleMessageResponses(String message, HttpStatus status) {
		Map<String, String> jsonResponse = new HashMap<>();
		jsonResponse.put("message", message);
		return new ResponseEntity<>(jsonResponse, status);
	}

	/**
	 * Generates an HTTP response body with a given entity.
	 * 
	 * @param data   The entity to send.
	 * @param status The HTTP's response status code.
	 * @return A JSON formatted response with the given message.
	 */
	public static ResponseEntity<?> handleDataResponses(Object data, HttpStatus status) {
		return new ResponseEntity<>(data, status);
	}

	/**
	 * Captures a ResponseStatusException instance to generate an HTTP response with
	 * the given exception's data
	 * 
	 * @param e The received exception instance.
	 * @return A JSON formatted response with the given exception's data.
	 */
	@ExceptionHandler(ResponseStatusException.class)
	public ResponseEntity<?> handleStatusExceptions(ResponseStatusException e) {
		return handleMessageResponses(e.getReason(), HttpStatus.resolve(e.getStatusCode().value()));
	}

	/**
	 * Captures a MethodArgumentNotValidException instance thrown by arguments with
	 * the {@link jakarta.validation.Valid @valid} annotation and uses the received data to generate an HTTP response
	 * 
	 * @param e The captured exception instance.
	 * @return A JSON formatted response with the given exception's data.
	 */
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<?> handleValidationExceptions(MethodArgumentNotValidException e) {
		Map<String, String> jsonResponse = new HashMap<>();
		e.getBindingResult().getAllErrors().forEach((error) -> {
			String fieldName = ((FieldError) error).getField();
			String errorMessage = error.getDefaultMessage();
			jsonResponse.put(fieldName, errorMessage);
		});
		return new ResponseEntity<>(jsonResponse, HttpStatus.BAD_REQUEST);
	}

	/**
	 * Captures a ConstraintViolationException instance thrown by controller classes
	 * with the {@link org.springframework.validation.annotation.Validated @Validated} annotation and uses and uses the received data to
	 * generate an HTTP response.
	 * 
	 * @param e The captured exception instance.
	 * @return A JSON formatted response with the given exception's data.
	 */
	@ExceptionHandler(ConstraintViolationException.class)
	public ResponseEntity<?> handleConstraintValidationExceptions(ConstraintViolationException e) {
		Map<String, String> jsonResponse = new HashMap<>();
		e.getConstraintViolations().forEach(violation -> {
			String message = violation.getMessage();
			jsonResponse.put("message", message);
		});
		return new ResponseEntity<>(jsonResponse, HttpStatus.BAD_REQUEST);
	}

}

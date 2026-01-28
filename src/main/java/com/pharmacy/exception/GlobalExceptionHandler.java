/* Copyright (C) Pharmacy Management System - All Rights Reserved */
package com.pharmacy.exception;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import lombok.Getter;
import lombok.Setter;

@ControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(RuntimeException.class)
  public ResponseEntity<ErrorResponse> handleRuntimeException(RuntimeException ex) {
    // Log the error (optional, add logger if needed)
    // logger.error("Error occurred", ex);
    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
        .body(new ErrorResponse("ERROR", ex.getMessage()));
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<Map<String, String>> handleValidationExceptions(
      MethodArgumentNotValidException ex) {
    Map<String, String> errors = new HashMap<>();
    ex.getBindingResult()
        .getAllErrors()
        .forEach(
            (error) -> {
              String fieldName = ((FieldError) error).getField();
              String errorMessage = error.getDefaultMessage();
              errors.put(fieldName, errorMessage);
            });
    return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
  }

  // Add more handlers for specific exceptions as needed

  @Setter
  @Getter
  public static class ErrorResponse {
    private String status;
    private String message;

    public ErrorResponse(String status, String message) {
      this.status = status;
      this.message = message;
    }
  }
}

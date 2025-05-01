package com.carlos.devices.repository;

import com.carlos.devices.domain.exception.BusinessRulesException;
import com.carlos.devices.domain.exception.DataException;
import com.carlos.devices.domain.model.ErrorDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

/**
 * Global exception handler for managing and formatting error responses in a REST API.
 *
 * This class uses Spring's {@link ControllerAdvice} and {@link ExceptionHandler} annotations
 * to centralize the handling of exceptions thrown by controllers in the application.
 * It converts exceptions into structured error responses, maintaining consistency
 * across all API responses.
 *
 * Error Responses:
 * Each handler method returns a {@link ResponseEntity} wrapping an {@link ErrorDTO} object.
 * The {@link ErrorDTO} encapsulates the error code and a descriptive error message,
 * making it easier for clients to understand the issue.
 *
 * Exception Handlers:
 * - {@code BusinessRulesException} results in a "BUSINESS_ERROR" response with an HTTP 400 status.
 * - {@code DataException} results in a "DATA_ERROR" response with an HTTP 404 status.
 * - {@code NoResourceFoundException} results in an "INVALID_REQUEST" response with an HTTP 400 status.
 * - {@code MethodArgumentTypeMismatchException} results in an "INVALID_REQUEST" response with an HTTP 400 status.
 * - Generic {@code Exception} results in a "SYSTEM_ERROR" response with an HTTP 500 status.
 *
 * Purpose:
 * This class ensures uniform error formatting and simplifies debugging by clearly categorizing
 * errors into business, data, and system-level exceptions. It promotes a clean separation
 * of concerns by abstracting exception management from controller logic.
 */
@ControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(value = BusinessRulesException.class)
    public ResponseEntity<ErrorDTO> businessException(BusinessRulesException businessRulesException) {
        return new ResponseEntity<>(new ErrorDTO("BUSINESS_ERROR", businessRulesException.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = DataException.class)
    public ResponseEntity<ErrorDTO> dataException(DataException exception) {
        return new ResponseEntity<>(new ErrorDTO("DATA_ERROR", exception.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = NoResourceFoundException.class)
    public ResponseEntity<ErrorDTO> invalidRequest(Exception exception) {
        return new ResponseEntity<>(new ErrorDTO("INVALID_REQUEST", exception.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorDTO> invalidRequestParameter(MethodArgumentTypeMismatchException exception) {
        return new ResponseEntity<>(new ErrorDTO("INVALID_REQUEST", exception.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<ErrorDTO> generalHandler(Exception exception) {
        return new ResponseEntity<>(new ErrorDTO("SYSTEM_ERROR", exception.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

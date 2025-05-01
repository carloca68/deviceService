package com.carlos.devices.repository;

import com.carlos.devices.domain.exception.BusinessRulesException;
import com.carlos.devices.domain.model.ErrorDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@ControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(value = BusinessRulesException.class)
    public ResponseEntity<ErrorDTO> businessException(BusinessRulesException businessRulesException) {
        return new ResponseEntity<>(new ErrorDTO("BUSINESS_ERROR", businessRulesException.getMessage()), HttpStatus.BAD_REQUEST);
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

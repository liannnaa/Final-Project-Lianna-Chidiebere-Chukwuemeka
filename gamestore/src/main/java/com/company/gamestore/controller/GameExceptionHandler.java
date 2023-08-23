package com.company.gamestore.controller;

import com.company.gamestore.exception.GameNotFoundException;
import com.company.gamestore.exception.InvalidGameException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@ControllerAdvice
@RestController
public class GameExceptionHandler {

    @ExceptionHandler(GameNotFoundException.class)
    public final ResponseEntity<String> handleGameNotFoundException(GameNotFoundException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InvalidGameException.class)
    public final ResponseEntity<String> handleInvalidGameException(InvalidGameException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public final ResponseEntity<String> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        String error = "Invalid ID format. Please provide a valid integer ID.";
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public final ResponseEntity<String> handleInvalidRequestMethod(HttpRequestMethodNotSupportedException ex) {
        String error = "Invalid request method. Please check your request type.";
        return new ResponseEntity<>(error, HttpStatus.METHOD_NOT_ALLOWED);
    }
}
package com.company.gamestore.controller;

import com.company.gamestore.exception.NotFoundException;
import com.company.gamestore.exception.InvalidException;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@org.springframework.web.bind.annotation.ControllerAdvice
@RestController
public class ControllerAdvice implements ErrorController {

    private static final String PATH = "/error";

    @RequestMapping(PATH)
    public ResponseEntity<String> error() {
        // Custom message for 404
        return new ResponseEntity<>("The requested endpoint was not found.", HttpStatus.NOT_FOUND);
    }

    public String getErrorPath() {
        return null;
    }

    @ExceptionHandler(NotFoundException.class)
    public final ResponseEntity<String> handleNotFoundException(NotFoundException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InvalidException.class)
    public final ResponseEntity<String> handleInvalidException(InvalidException ex) {
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

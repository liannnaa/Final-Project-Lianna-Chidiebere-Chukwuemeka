package com.company.gamestore.controller;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CustomErrorController implements ErrorController {

    private static final String PATH = "/error";

    @RequestMapping(PATH)
    public ResponseEntity<String> error() {
        // Custom message for 404
        return new ResponseEntity<>("The requested endpoint was not found.", HttpStatus.NOT_FOUND);
    }

    public String getErrorPath() {
        return null;
    }
}

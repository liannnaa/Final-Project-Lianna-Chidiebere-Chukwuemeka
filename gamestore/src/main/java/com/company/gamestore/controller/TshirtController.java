package com.company.gamestore.controller;

import com.company.gamestore.exception.InvalidException;
import com.company.gamestore.exception.NotFoundException;
import com.company.gamestore.model.Tshirt;
import com.company.gamestore.service.TshirtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/tshirts")
public class TshirtController {
    @Autowired
    TshirtService tshirtService;

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleNotFound(NotFoundException ex) {
        return ex.getMessage();
    }

    @ExceptionHandler(InvalidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleInvalid(InvalidException ex) {
        return ex.getMessage();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Tshirt addTshirt(@RequestBody Tshirt tshirt) {
        return tshirtService.addTshirt(tshirt);
    }

    @GetMapping
    public List<Tshirt> getAllTshirts() {
        return tshirtService.getAllTshirts();
    }

    @GetMapping("/{id}")
    public Optional<Tshirt> getTshirtById(@PathVariable int id) {
        return tshirtService.getTshirtById(id);
    }

    @GetMapping("/color/{color}")
    public List<Tshirt> findByColor(@PathVariable String color) {
        return tshirtService.findByColor(color);
    }

    @GetMapping("/size/{size}")
    public List<Tshirt> findBySize(@PathVariable String size) {
        return tshirtService.findBySize(size);
    }

    @PutMapping
    public Tshirt updateTshirt(@RequestBody Tshirt console) {
        return tshirtService.updateTshirt(console);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTshirt(@PathVariable int id) {
        tshirtService.deleteTshirt(id);
    }

}
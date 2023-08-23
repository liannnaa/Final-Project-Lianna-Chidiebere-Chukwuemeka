package com.company.gamestore.controller;

import com.company.gamestore.exception.InvalidException;
import com.company.gamestore.exception.NotFoundException;
import com.company.gamestore.model.Console;
import com.company.gamestore.service.ConsoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/consoles")
public class ConsoleController {
    @Autowired
    ConsoleService consoleService;

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
    public Console addConsole(@RequestBody Console console) {
        return consoleService.addConsole(console);
    }

    @GetMapping
    public List<Console> getAllConsoles() {
        return consoleService.getAllConsoles();
    }

    @GetMapping("/{id}")
    public Optional<Console> getConsoleById(@PathVariable int id) {
        return consoleService.getConsoleById(id);
    }

    @GetMapping("/manufacturer/{manufacturer}")
    public List<Console> findByManufacturer(@PathVariable String manufacturer) {
        return consoleService.findByManufacturer(manufacturer);
    }

    @PutMapping
    public Console updateConsole(@RequestBody Console console) {
        return consoleService.updateConsole(console);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteConsole(@PathVariable int id) {
        consoleService.deleteConsole(id);
    }

}

package com.company.gamestore.controller;

import com.company.gamestore.model.Console;
import com.company.gamestore.repository.ConsoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.Optional;

@Controller
public class ConsoleGraphController {
    @Autowired
    ConsoleRepository consoleRepository;

    @QueryMapping
    public List<Console> getAllConsoles() {
        return consoleRepository.findAll();
    }

    @QueryMapping
    public Console getConsoleById(@Argument int id) {
        Optional<Console> returnVal = consoleRepository.findById(id);
        return returnVal.orElse(null);
    }

    @QueryMapping
    public List<Console> getConsolesByManufacturer(@Argument String manufacturer) {
        return consoleRepository.findByManufacturer(manufacturer);
    }
}
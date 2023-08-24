package com.company.gamestore.service;

import com.company.gamestore.exception.InvalidException;
import com.company.gamestore.exception.NotFoundException;
import com.company.gamestore.model.Console;
import com.company.gamestore.repository.ConsoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class ConsoleService {
    private final ConsoleRepository consoleRepository;

    @Autowired
    public ConsoleService(ConsoleRepository consoleRepository){
        this.consoleRepository = consoleRepository;
    }

    public Console addConsole(Console console) {
        if (console.getModel() == null || console.getManufacturer() == null || console.getPrice() == null || console.getQuantity() == 0) {
            throw new InvalidException("All console fields must be filled.");
        }
        if (console.getPrice() < 0){
            throw new InvalidException("Console price must be greater than 0.");
        }

        return consoleRepository.save(console);
    }

    public List<Console> getAllConsoles() {
        List<Console> consoles = consoleRepository.findAll();
        if (consoles == null || consoles.isEmpty()) {
            throw new NotFoundException("No consoles found.");
        }
        return consoles;
    }

    public Optional<Console> getConsoleById(int id) {
        if (!consoleRepository.existsById(id)) {
            throw new NotFoundException("Console not found with id: " + id);
        }
        return consoleRepository.findById(id);
    }

    public List<Console> findByManufacturer(String manufacturer) {
        List<Console> consolesByManufacturer = consoleRepository.findByManufacturer(manufacturer);
        if (consolesByManufacturer == null || consolesByManufacturer.isEmpty()) {
            throw new NotFoundException("No consoles found with manufacturer: " + manufacturer);
        }

        return consolesByManufacturer;
    }

    public Console updateConsole(Console console) {
        if (!consoleRepository.existsById(console.getConsoleId())) {
            throw new NotFoundException("Console not found with id: " + console.getConsoleId());
        }
        if (console.getModel() == null || console.getManufacturer() == null || console.getPrice() == null || console.getQuantity() == 0) {
            throw new InvalidException("All console fields must be filled.");
        }
        if (console.getPrice() < 0){
            throw new InvalidException("Console price must be greater than 0.");
        }

        return consoleRepository.save(console);
    }

    public void deleteConsole(int id) {
        if (!consoleRepository.existsById(id)) {
            throw new NotFoundException("Console not found with id: " + id);
        }
        consoleRepository.deleteById(id);
    }
}

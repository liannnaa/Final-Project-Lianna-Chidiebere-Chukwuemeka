package com.company.gamestore.repository;

import com.company.gamestore.model.Console;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class ConsoleRepositoryTest {

    @Autowired
    ConsoleRepository consoleRepo;

    private Console console;

    @BeforeEach
    public void setUp(){
        consoleRepo.deleteAll();

        // Arrange
        console = new Console();
        console.setModel("Test Model");
        console.setManufacturer("Test Manufacturer");
        console.setMemoryAmount("Test Amount");
        console.setProcessor("Test Processor");
        console.setPrice(59.99);
        console.setQuantity(10);
        console = consoleRepo.save(console);
    }

    @Test
    public void addConsole(){
        // Assert
        Optional<Console> foundConsole = consoleRepo.findById(console.getConsoleId());
        assertEquals(foundConsole.get(), console);
    }

    @Test
    public void updateConsole(){
        // Act
        console.setModel("UPDATED MODEL");
        consoleRepo.save(console);

        // Assert
        Optional<Console> foundConsole = consoleRepo.findById(console.getConsoleId());
        assertEquals(foundConsole.get(), console);
    }

    @Test
    public void deleteConsole(){
        // Act
        consoleRepo.deleteById(console.getConsoleId());

        // Assert
        Optional<Console> foundConsole = consoleRepo.findById(console.getConsoleId());
        assertFalse(foundConsole.isPresent());
    }

    @Test
    public void getConsoleById(){
        // Act
        Console foundConsole = consoleRepo.findById(console.getConsoleId()).orElse(null);

        // Assert
        assertEquals(console, foundConsole);
    }

    @Test
    public void getConsoleByManufacturer(){
        // Arrange
        Console console2 = new Console();
        console2.setModel("Test Model 2");
        console2.setManufacturer("Test Manufacturer 2");
        console2.setMemoryAmount("Test Amount 2");
        console2.setProcessor("Test Processor 2");
        console2.setPrice(69.99);
        console2.setQuantity(20);
        consoleRepo.save(console2);

        // Act
        List<Console> consoles = consoleRepo.findByManufacturer(console.getManufacturer());

        // Assert
        assertTrue(consoles.contains(console));
        assertFalse(consoles.contains(console2));
    }
}

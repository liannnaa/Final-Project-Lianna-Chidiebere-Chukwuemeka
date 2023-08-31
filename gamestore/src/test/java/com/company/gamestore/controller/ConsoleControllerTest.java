package com.company.gamestore.controller;

import com.company.gamestore.exception.InvalidException;
import com.company.gamestore.exception.NotFoundException;
import com.company.gamestore.model.Console;
import com.company.gamestore.service.ConsoleService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Collections;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@WebMvcTest(ConsoleController.class)
@AutoConfigureMockMvc(addFilters = false)
public class ConsoleControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ConsoleService consoleService;

    private final ObjectMapper mapper = new ObjectMapper();

    private Console console;

    @BeforeEach
    public void setUp(){
        console = new Console();
        console.setConsoleId(1);
        console.setModel("Test Model");
        console.setManufacturer("Test Manufacturer");
        console.setMemoryAmount("Test Amount");
        console.setProcessor("Test Processor");
        console.setPrice(1.00);
        console.setQuantity(1);
    }

    @Test
    public void addConsoleTest() throws Exception{
        when(consoleService.addConsole(any(Console.class))).thenReturn(console);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/consoles")
                        .content(mapper.writeValueAsString(console))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.model").value("Test Model"));
    }

    @Test
    public void getConsoleByIdTest() throws Exception {
        when(consoleService.getConsoleById(1)).thenReturn(Optional.ofNullable(console));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/consoles/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.model").value("Test Model"));
    }

    @Test
    public void getAllConsolesTest() throws Exception {
        when(consoleService.getAllConsoles()).thenReturn(Collections.singletonList(console));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/consoles"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].model").value("Test Model"));
    }

    @Test
    public void updateConsoleTest() throws Exception {
        when(consoleService.updateConsole(any(Console.class))).thenReturn(console);

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/consoles")
                        .content(mapper.writeValueAsString(console))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.model").value("Test Model"));
    }

    @Test
    public void deleteConsoleTest() throws Exception {
        doNothing().when(consoleService).deleteConsole(1);

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/consoles/{id}", 1))
                .andExpect(status().isNoContent());
    }

    @Test
    public void getConsoleByManufacturerTest() throws Exception {
        when(consoleService.findByManufacturer("Test Manufacturer")).thenReturn(Collections.singletonList(console));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/consoles/manufacturer/{manufacturer}", "Test Manufacturer"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].model").value("Test Model"));
    }

    @Test
    public void addConsolePriceBelowZeroTest() throws Exception {
        doThrow(new InvalidException("Console price must be greater than 0.")).when(consoleService).addConsole(any(Console.class));

        Console invalidConsolePrice = new Console();
        invalidConsolePrice.setPrice(-1.00);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/consoles")
                        .content(mapper.writeValueAsString(invalidConsolePrice))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Console price must be greater than 0."));
    }

    @Test
    public void getAllConsolesNotFoundTest() throws Exception {
        doThrow(new NotFoundException("No consoles found.")).when(consoleService).getAllConsoles();

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/consoles"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("No consoles found."));
    }

    @Test
    public void findByManufacturerNotFoundTest() throws Exception {
        doThrow(new NotFoundException("No consoles found with manufacturer: Test Manufacturer")).when(consoleService).findByManufacturer("Test Manufacturer");

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/consoles/manufacturer/{manufacturer}", "Test Manufacturer"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("No consoles found with manufacturer: Test Manufacturer"));
    }

    @Test
    public void updateConsoleNotFoundTest() throws Exception {
        doThrow(new NotFoundException("Console not found with id: 1")).when(consoleService).updateConsole(any(Console.class));

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/consoles")
                        .content(mapper.writeValueAsString(console))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Console not found with id: 1"));
    }

    @Test
    public void deleteConsoleNotFoundTest() throws Exception {
        doThrow(new NotFoundException("Console not found with id: 1")).when(consoleService).deleteConsole(1);

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/consoles/{id}", 1))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Console not found with id: 1"));
    }

}
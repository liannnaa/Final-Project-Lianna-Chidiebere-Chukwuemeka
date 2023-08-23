package com.company.gamestore.controller;

import com.company.gamestore.model.Console;
import com.company.gamestore.service.ConsoleService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Collections;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ConsoleController.class)
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

}
package com.company.gamestore.controller;

import com.company.gamestore.model.Tshirt;
import com.company.gamestore.service.TshirtService;
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

@WebMvcTest(TshirtController.class)
public class TshirtControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TshirtService tshirtService;

    private final ObjectMapper mapper = new ObjectMapper();

    private Tshirt tshirt;

    @BeforeEach
    public void setUp(){
        tshirt = new Tshirt();
        tshirt.setTshirtId(1);
        tshirt.setSize("Test Size");
        tshirt.setColor("Test Color");
        tshirt.setDescription("Test Description");
        tshirt.setPrice(1.00);
        tshirt.setQuantity(1);
    }

    @Test
    public void addTshirtTest() throws Exception{
        when(tshirtService.addTshirt(any(Tshirt.class))).thenReturn(tshirt);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/tshirts")
                        .content(mapper.writeValueAsString(tshirt))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.size").value("Test Size"));
    }

    @Test
    public void getTshirtByIdTest() throws Exception {
        when(tshirtService.getTshirtById(1)).thenReturn(Optional.ofNullable(tshirt));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/tshirts/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size").value("Test Size"));
    }

    @Test
    public void getAllTshirtTest() throws Exception {
        when(tshirtService.getAllTshirts()).thenReturn(Collections.singletonList(tshirt));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/tshirts"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].size").value("Test Size"));
    }

    @Test
    public void updateTshirtTest() throws Exception {
        when(tshirtService.updateTshirt(any(Tshirt.class))).thenReturn(tshirt);

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/tshirts")
                        .content(mapper.writeValueAsString(tshirt))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size").value("Test Size"));
    }

    @Test
    public void deleteTshirtTest() throws Exception {
        doNothing().when(tshirtService).deleteTshirt(1);

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/tshirts/{id}", 1))
                .andExpect(status().isNoContent());
    }

    @Test
    public void getTshirtByColorTest() throws Exception {
        when(tshirtService.findByColor("Test Color")).thenReturn(Collections.singletonList(tshirt));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/tshirts/color/{color}", "Test Color"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].size").value("Test Size"));
    }

    @Test
    public void getTshirtBySizeTest() throws Exception {
        when(tshirtService.findBySize("Test Size")).thenReturn(Collections.singletonList(tshirt));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/tshirts/size/{size}", "Test Size"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].color").value("Test Color"));
    }
}
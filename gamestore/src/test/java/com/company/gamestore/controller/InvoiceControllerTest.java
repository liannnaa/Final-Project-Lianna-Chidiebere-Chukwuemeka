package com.company.gamestore.controller;

import com.company.gamestore.exception.NotFoundException;
import com.company.gamestore.model.Invoice;
import com.company.gamestore.service.InvoiceService;
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

import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(InvoiceController.class)
@AutoConfigureMockMvc(addFilters = false)
public class InvoiceControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private InvoiceService invoiceService;

    private final ObjectMapper mapper = new ObjectMapper();
    private Invoice invoice;

    @BeforeEach
    public void setup() {
        invoice = new Invoice();
        invoice.setInvoiceId(1);
        invoice.setName("Test Customer");
    }

    @Test
    public void createInvoiceTest() throws Exception {
        when(invoiceService.createInvoice(any(Invoice.class))).thenReturn(invoice);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/invoices")
                        .content(mapper.writeValueAsString(invoice))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Test Customer"));
    }

    @Test
    public void getInvoiceByIdTest() throws Exception {
        when(invoiceService.getInvoiceById(1)).thenReturn(invoice);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/invoices/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Test Customer"));
    }


    @Test
    public void getAllInvoicesTest() throws Exception {
        when(invoiceService.getAllInvoices()).thenReturn(Collections.singletonList(invoice));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/invoices"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Test Customer"));
    }

    @Test
    public void getInvoicesByCustomerNameTest() throws Exception {
        when(invoiceService.getInvoicesByCustomerName("Test Customer")).thenReturn(Collections.singletonList(invoice));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/invoices/customer/{name}", "Test Customer"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Test Customer"));
    }

    @Test
    public void getInvoiceByIdNotFoundTest() throws Exception {
        doThrow(new NotFoundException("Invoice not found with id: 1")).when(invoiceService).getInvoiceById(1);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/invoices/{id}", 1))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Invoice not found with id: 1"));
    }
}

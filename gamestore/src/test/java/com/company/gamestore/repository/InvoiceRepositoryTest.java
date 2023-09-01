package com.company.gamestore.repository;

import com.company.gamestore.model.Invoice;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
public class InvoiceRepositoryTest {

    @Autowired
    InvoiceRepository invoiceRepo;

    private Invoice invoice;

    @BeforeEach
    public void setUp() {
        invoiceRepo.deleteAll();

        // Arrange
        invoice = new Invoice();
        invoice.setName("Test Name");
        invoice.setStreet("test");
        invoice.setCity("test");
        invoice.setState("test");
        invoice.setZipcode("test");
        invoice.setItemType("Game");
        invoice.setItemId(1);
        invoice.setQuantity(1);
        invoice = invoiceRepo.save(invoice);
    }

    @Test
    public void addInvoice() {
        // Assert
        Optional<Invoice> foundInvoice = invoiceRepo.findById(invoice.getInvoiceId());
        assertEquals(foundInvoice.get(), invoice);
    }

    @Test
    public void getInvoiceById() {
        // Act
        Invoice foundInvoice = invoiceRepo.findById(invoice.getInvoiceId()).orElse(null);

        // Assert
        assertEquals(invoice, foundInvoice);
    }

    @Test
    public void getInvoicesByName() {
        // Act
        List<Invoice> invoices = invoiceRepo.findByName(invoice.getName());

        // Assert
        assertTrue(invoices.contains(invoice));
    }
}

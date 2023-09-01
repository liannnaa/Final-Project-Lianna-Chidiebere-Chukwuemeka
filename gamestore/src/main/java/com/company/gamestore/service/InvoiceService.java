package com.company.gamestore.service;

import com.company.gamestore.model.*;
import com.company.gamestore.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Optional;

@Service
public class InvoiceService {

    @Autowired
    private InvoiceRepository invoiceRepository;

    @Autowired
    private TaxRepository taxRepository;

    @Autowired
    private FeeRepository feeRepository;

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private ConsoleRepository consoleRepository;

    @Autowired
    private TshirtRepository tshirtRepository;

    // Method to create an invoice
    public Invoice createInvoice(Invoice invoice) {
        // Validation
        if (invoice.getQuantity() <= 0) {
            throw new IllegalArgumentException("Order quantity must be greater than zero.");
        }

        // Initialize variables
        int availableInventory = 0;
        BigDecimal unitPrice = BigDecimal.ZERO;

        // Checking available inventory and unit price based on itemType
        switch (invoice.getItemType()) {
            case "Game":
                Game game = gameRepository.findById(invoice.getItemId()).orElseThrow(() ->
                        new IllegalArgumentException("Game with ID " + invoice.getItemId() + " not found."));
                availableInventory = game.getQuantity();
                unitPrice = BigDecimal.valueOf(game.getPrice());
                break;
            case "Console":
                Console console = consoleRepository.findById(invoice.getItemId()).orElseThrow(() ->
                        new IllegalArgumentException("Console with ID " + invoice.getItemId() + " not found."));
                availableInventory = console.getQuantity();
                unitPrice = BigDecimal.valueOf(console.getPrice());
                break;
            case "Tshirt":
                Tshirt tshirt = tshirtRepository.findById(invoice.getItemId()).orElseThrow(() ->
                        new IllegalArgumentException("Tshirt with ID " + invoice.getItemId() + " not found."));
                availableInventory = tshirt.getQuantity();
                unitPrice = BigDecimal.valueOf(tshirt.getPrice());
                break;
            default:
                throw new IllegalArgumentException("Invalid item type provided.");
        }

        // Set the unitPrice in the invoice
        invoice.setUnitPrice(unitPrice);

        // Checking available inventory
        if (invoice.getQuantity() > availableInventory) {
            throw new IllegalArgumentException("Order quantity exceeds available inventory.");
        }

        Optional<Tax> taxRateOptional = taxRepository.findTaxRateByState(invoice.getState());
        if (!taxRateOptional.isPresent()) {
            throw new IllegalArgumentException("Invalid state code provided.");
        }

        // Calculation
        BigDecimal subtotal = unitPrice.multiply(new BigDecimal(invoice.getQuantity()));
        BigDecimal taxAmount = subtotal.multiply(taxRateOptional.get().getRate()).setScale(2, RoundingMode.HALF_UP);

        Optional<Fee> feeOptional = feeRepository.findByProductType(invoice.getItemType());
        if (!feeOptional.isPresent()) {
            throw new IllegalArgumentException("Invalid item type provided.");
        }
        BigDecimal processingFee = feeOptional.get().getFee();
        if (invoice.getQuantity() > 10) {
            processingFee = processingFee.add(new BigDecimal("15.49"));
        }

        BigDecimal total = subtotal.add(taxAmount).add(processingFee).setScale(2, RoundingMode.HALF_UP);

        // Setting the calculated values to the invoice
        invoice.setSubtotal(subtotal);
        invoice.setTax(taxAmount);
        invoice.setProcessingFee(processingFee);
        invoice.setTotal(total);

        // Persistence
        return invoiceRepository.save(invoice);
    }


    // Method to retrieve an invoice by ID
    public Invoice getInvoiceById(int id) {
        return invoiceRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invoice with ID " + id + " not found."));
    }

    // Method to retrieve all invoices
    public List<Invoice> getAllInvoices() {
        return invoiceRepository.findAll();
    }

    // Method to retrieve invoices by customer name
    public List<Invoice> getInvoicesByCustomerName(String name) {
        return invoiceRepository.findByName(name);
    }
}

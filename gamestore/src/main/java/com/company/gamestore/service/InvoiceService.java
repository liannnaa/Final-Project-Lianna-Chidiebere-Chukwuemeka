package com.company.gamestore.service;

import com.company.gamestore.model.Invoice;
import com.company.gamestore.model.Tax;
import com.company.gamestore.model.Fee;
import com.company.gamestore.repository.InvoiceRepository;
import com.company.gamestore.repository.TaxRepository;
import com.company.gamestore.repository.FeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
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

    // Method to create an invoice
    public Invoice createInvoice(Invoice invoice) {
        // Validation
        if (invoice.getQuantity() <= 0) {
            throw new IllegalArgumentException("Order quantity must be greater than zero.");
        }

        // Placeholder for checking available inventory
        // if (invoice.getQuantity() > availableInventory) {
        //     throw new IllegalArgumentException("Order quantity exceeds available inventory.");
        // }

        Optional<Tax> taxRateOptional = taxRepository.findById(invoice.getState());
        if (!taxRateOptional.isPresent()) {
            throw new IllegalArgumentException("Invalid state code provided.");
        }

        // Calculation
        BigDecimal subtotal = invoice.getUnitPrice().multiply(new BigDecimal(invoice.getQuantity()));
        BigDecimal taxAmount = subtotal.multiply(taxRateOptional.get().getRate()).setScale(2, BigDecimal.ROUND_HALF_UP);

        Optional<Fee> feeOptional = feeRepository.findById(invoice.getItemType());
        if (!feeOptional.isPresent()) {
            throw new IllegalArgumentException("Invalid item type provided.");
        }
        BigDecimal processingFee = feeOptional.get().getFee();
        if (invoice.getQuantity() > 10) {
            processingFee = processingFee.add(new BigDecimal("15.49"));
        }

        BigDecimal total = subtotal.add(taxAmount).add(processingFee).setScale(2, BigDecimal.ROUND_HALF_UP);

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
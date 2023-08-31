package com.company.gamestore.service;

import com.company.gamestore.model.*;
import com.company.gamestore.repository.*;
import com.company.gamestore.viewmodel.InvoiceViewModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.Optional;

@Service
public class InvoiceService {

    @Autowired
    private InvoiceRepository invoiceRepository;
    private GameRepository gameRepository;
    private TshirtRepository tshirtRepository;

    private ConsoleRepository consoleRepository;
    private FeeRepository feeRepository;
    private TaxRepository taxRepository;

    @Autowired
    public InvoiceService(InvoiceRepository invRepo){
        this.invoiceRepository = invRepo;
    }


    public InvoiceViewModel buildInvoiceViewModel(Invoice invoice) {
        InvoiceViewModel inv = new InvoiceViewModel();

        // Assemble the view model
        inv.setId(invoice.getInvoiceId());
        inv.setName(invoice.getName());
        inv.setStreet(invoice.getStreet());
        inv.setCity(invoice.getCity());
        inv.setState(invoice.getState());
        inv.setZipcode(invoice.getZipcode());
        inv.setItemType(invoice.getItemType());
        inv.setItemId(invoice.getItemId());
        inv.setUnitPrice(invoice.getUnitPrice());
        inv.setQuantity(invoice.getQuantity());
        inv.setSubtotal(invoice.getSubtotal());
        inv.setTax(invoice.getTax());
        inv.setFee(invoice.getFee());   //need to create
        inv.setTotal(invoice.getTotal());

        return inv; // return the invoice view model
    }


    // SAVE(POST) an invoice
    // @Transactional
    public InvoiceViewModel saveInvoice(InvoiceViewModel viewModel){
        Invoice invoice = new Invoice();

        // Set what we know for invoice
        invoice.setName(viewModel.getName());
        invoice.setStreet(viewModel.getStreet());
        invoice.setCity(viewModel.getCity());
        invoice.setState(viewModel.getState());
        invoice.setZipcode(viewModel.getZipcode());
        invoice.setItemType(viewModel.getItemType());
        invoice.setItemId(viewModel.getItemId());
        invoice.setQuantity(viewModel.getQuantity());

        // Validate item type (Maybe find a way to reduce later)
        switch (viewModel.getItemType().toUpperCase()) {
            case "GAME":
                invoice.setItemType("GAME");

                Optional<Game> game = gameRepository.findById(invoice.getItemId());

                if(game.isEmpty()) // if not found
                    throw new IllegalArgumentException("Game not found.");


                if(game.get().getQuantity() < invoice.getQuantity()) // if not enough in stock
                    throw new IllegalArgumentException("Not enough games in stock.");
                else
                    game.get().setQuantity(game.get().getQuantity() - invoice.getQuantity()); // update stock

                break;
            case "TSHIRT":
                invoice.setItemType("TSHIRT");
                Optional<Tshirt> tShirt = tshirtRepository.findById(invoice.getItemId());

                if(tShirt.isEmpty()) // if not found
                    throw new IllegalArgumentException("Tshirt not found.");


                if(tShirt.get().getQuantity() < invoice.getQuantity()) // if not enough in stock
                    throw new IllegalArgumentException("Not enough Tshirts in stock.");
                else
                    tShirt.get().setQuantity(tShirt.get().getQuantity() - invoice.getQuantity()); // update stock

                break;
            case "CONSOLE":
                invoice.setItemType("CONSOLE");

                Optional<Tshirt> console = tshirtRepository.findById(invoice.getItemId());

                if(console.isEmpty()) // if not found
                    throw new IllegalArgumentException("Console not found.");


                if(console.get().getQuantity() < invoice.getQuantity()) // if not enough in stock
                    throw new IllegalArgumentException("Not enough consoles in stock.");
                else
                    console.get().setQuantity(console.get().getQuantity() - invoice.getQuantity()); // update stock

                break;
            default:
                throw new IllegalArgumentException("Not a valid item type");
        }
        viewModel.setItemType(invoice.getItemType()); // make sure view model matches


        // Calculate subtotal and set it
        BigDecimal subtotalFormatted = invoice.getSubtotal().setScale(2,BigDecimal.ROUND_HALF_UP);
        invoice.setSubtotal(subtotalFormatted);


        // Calculate tax rate
        Optional<Tax> stateTax = taxRepository.findTaxRateByState(invoice.getState());


        if(stateTax.isPresent()){
            BigDecimal salesTaxRate = stateTax.get().getRate(); // gets rate from optional above
            // formats the tax calculation
            BigDecimal taxFormatted = (salesTaxRate.multiply(invoice.getSubtotal())).setScale(2, BigDecimal.ROUND_HALF_UP);
            invoice.setTax(taxFormatted);// sets the tax rate
        }


        // Calculate processing fee
        Optional<Fee> processingFee = feeRepository.findByProductType(invoice.getItemType());

        if(processingFee.isPresent()){ // if processing Fee is present
            BigDecimal invoiceFee = processingFee.get().getFee();
            if(invoice.getQuantity() > 10){ // add additional fee if quantity > 10
                invoiceFee = invoiceFee.add(new BigDecimal("15.49"));
            }
            invoice.setFee(invoiceFee); // set invoice fee
        }
        // save the invoice
        invoice = invoiceRepository.save(invoice);

        // set Id of View Model
        viewModel.setId(invoice.getInvoiceId());

        return viewModel;
    }
}

package com.company.gamestore.service;

import com.company.gamestore.model.Fee;
import com.company.gamestore.model.Game;
import com.company.gamestore.model.Invoice;
import com.company.gamestore.model.Tax;
import com.company.gamestore.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@AutoConfigureMockMvc(addFilters = false)
public class InvoiceServiceTest {

    private InvoiceService invoiceService;
    private InvoiceRepository invoiceRepository;

    private TaxRepository taxRepository;
    private FeeRepository feeRepository;
    private ConsoleRepository consoleRepository;
    private TshirtRepository tshirtRepository;
    private GameRepository gameRepository;

    @BeforeEach
    public void setUp() {
        invoiceRepository = mock(InvoiceRepository.class);
        gameRepository = mock(GameRepository.class);
        taxRepository = mock(TaxRepository.class);
        feeRepository = mock(FeeRepository.class);
        consoleRepository = mock(ConsoleRepository.class);
        tshirtRepository = mock(TshirtRepository.class);

        invoiceService = new InvoiceService(
                invoiceRepository,
                taxRepository,
                feeRepository,
                gameRepository,
                consoleRepository,
                tshirtRepository
        );

        gameRepository = mock(GameRepository.class);

        Invoice mockInvoice = new Invoice();
        mockInvoice.setInvoiceId(1);
        mockInvoice.setName("John Doe");

        when(invoiceRepository.findById(1)).thenReturn(Optional.of(mockInvoice));
        when(invoiceRepository.findAll()).thenReturn(Arrays.asList(mockInvoice));
        when(invoiceRepository.findByName("John Doe")).thenReturn(Arrays.asList(mockInvoice));
        when(invoiceRepository.save(any(Invoice.class))).thenReturn(mockInvoice);
    }

    @Test
    public void testCreateInvoice() {
        Game game = new Game();
        game.setGameId(1);
        game.setTitle("Test Title");
        game.setEsrbRating("Test Rating");
        game.setDescription("Test Description");
        game.setPrice(1.00);
        game.setStudio("Test Studio");
        game.setQuantity(1);

        when(gameRepository.findById(1)).thenReturn(Optional.of(game));

        Tax tax = new Tax();
        tax.setState("CA");
        tax.setRate(new BigDecimal("0.1"));

        when(taxRepository.findTaxRateByState("CA")).thenReturn(Optional.of(tax));

        Fee fee = new Fee();
        fee.setProductType("Game");
        fee.setFee(new BigDecimal("2"));

        when(feeRepository.findByProductType("Game")).thenReturn(Optional.of(fee));

        Invoice invoice = new Invoice();
        invoice.setItemId(1);
        invoice.setItemType("Game");
        invoice.setQuantity(5);

        when(invoiceRepository.save(any(Invoice.class))).thenAnswer(i -> i.getArguments()[0]);

        Invoice savedInvoice = invoiceService.createInvoice(invoice);

        assertEquals(new BigDecimal("20.00"), savedInvoice.getUnitPrice());
        assertEquals(new BigDecimal("100.00"), savedInvoice.getSubtotal());
        assertEquals(new BigDecimal("10.00"), savedInvoice.getTax());
        assertEquals(new BigDecimal("2.00"), savedInvoice.getProcessingFee());
        assertEquals(new BigDecimal("112.00"), savedInvoice.getTotal());
    }

    @Test
    public void testCreateInvoiceInvalidQuantity() {
        Invoice invoice = new Invoice();
        invoice.setItemId(1);
        invoice.setItemType("Game");
        invoice.setQuantity(0);

        assertThrows(IllegalArgumentException.class, () -> {
            invoiceService.createInvoice(invoice);
        });
    }

    @Test
    public void testCreateInvoiceInvalidState() {
        Invoice invoice = new Invoice();
        invoice.setItemId(1);
        invoice.setItemType("Game");
        invoice.setQuantity(5);

        Game game = new Game();
        game.setGameId(1);
        game.setPrice(20.0);
        game.setQuantity(10);

        when(gameRepository.findById(1)).thenReturn(Optional.of(game));
        when(taxRepository.findTaxRateByState("XYZ")).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> {
            invoiceService.createInvoice(invoice);
        });
    }

    @Test
    public void testGetInvoiceById() {
        Invoice retrievedInvoice = invoiceService.getInvoiceById(1);

        assertEquals(1, retrievedInvoice.getInvoiceId());
        assertEquals("John Doe", retrievedInvoice.getName());
    }

    @Test
    public void testGetAllInvoices() {
        List<Invoice> invoiceList = invoiceService.getAllInvoices();
        assertEquals(1, invoiceList.size());
        assertEquals("John Doe", invoiceList.get(0).getName());
    }

    @Test
    public void testGetInvoicesByCustomerName() {
        List<Invoice> invoiceList = invoiceService.getInvoicesByCustomerName("John Doe");
        assertEquals(1, invoiceList.size());
        assertEquals("John Doe", invoiceList.get(0).getName());
    }

    @Test
    public void testInvalidInvoiceCreation() {
        Invoice invalidInvoice = new Invoice();
        invalidInvoice.setName("Invalid Customer");
        invalidInvoice.setTotal(new BigDecimal("-10.00"));

        Exception exception = assertThrows(IllegalArgumentException.class, () -> invoiceService.createInvoice(invalidInvoice));
        assertEquals("Order quantity must be greater than zero.", exception.getMessage());
    }

    @Test
    public void testInvoiceNotFound() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> invoiceService.getInvoiceById(999));
        assertTrue(invoiceService.getInvoicesByCustomerName("Unknown Customer").isEmpty());
    }

}

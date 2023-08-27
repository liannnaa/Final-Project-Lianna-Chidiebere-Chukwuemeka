package com.company.gamestore.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Objects;

@Entity
@Table(name="console")
public class Console {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int consoleId;
    @NotNull(message = "Model cannot be null")
    private String model;
    @NotNull(message = "Manufacturer cannot be null")
    private String manufacturer;
    @NotNull(message = "Memory Amount cannot be null")
    private String memoryAmount;
    @NotNull(message = "Processor cannot be null")
    private String processor;
    @NotNull(message = "Price cannot be null")
    private Double price;
    @NotNull(message = "Quantity cannot be null")
    private int quantity;

    public int getConsoleId() {
        return consoleId;
    }

    public void setConsoleId(int consoleId) {
        this.consoleId = consoleId;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getMemoryAmount() {
        return memoryAmount;
    }

    public void setMemoryAmount(String memoryAmount) {
        this.memoryAmount = memoryAmount;
    }

    public String getProcessor() {
        return processor;
    }

    public void setProcessor(String processor) {
        this.processor = processor;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Console console = (Console) o;
        return consoleId == console.consoleId && quantity == console.quantity && Objects.equals(model, console.model) && Objects.equals(manufacturer, console.manufacturer) && Objects.equals(memoryAmount, console.memoryAmount) && Objects.equals(processor, console.processor) && Objects.equals(price, console.price);
    }

    @Override
    public int hashCode() {
        return Objects.hash(consoleId, model, manufacturer, memoryAmount, processor, price, quantity);
    }
}

package com.company.gamestore.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Objects;

@Entity
@Table(name="tshirt")
public class Tshirt {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int tshirtId;
    @NotNull(message = "Size cannot be null")
    private String size;
    @NotNull(message = "Color cannot be null")
    private String color;
    @NotNull(message = "Description cannot be null")
    private String description;
    @NotNull(message = "Price cannot be null")
    private Double price;
    @NotNull(message = "Quantity cannot be null")
    private int quantity;

    public int getTshirtId() {
        return tshirtId;
    }

    public void setTshirtId(int tshirtId) {
        this.tshirtId = tshirtId;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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
        Tshirt tshirt = (Tshirt) o;
        return tshirtId == tshirt.tshirtId && quantity == tshirt.quantity && Objects.equals(size, tshirt.size) && Objects.equals(color, tshirt.color) && Objects.equals(description, tshirt.description) && Objects.equals(price, tshirt.price);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tshirtId, size, color, description, price, quantity);
    }
}

package com.company.gamestore.model;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name="tax")
public class Tax {

    @Id
    @Column(length = 2)
    private String state;
    private Double rate;

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Double getRate() {
        return rate;
    }

    public void setRate(Double rate) {
        this.rate = rate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tax tax = (Tax) o;
        return Objects.equals(state, tax.state) && Objects.equals(rate, tax.rate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(state, rate);
    }
}

package com.combat47.ecommerce.catalog.domain.model;

import com.combat47.ecommerce.catalog.domain.exception.InvalidQuantityException;

import java.util.Objects;

public class Quantity {

    private final int value;


    public Quantity(int value) {
        if (value < 0) {
            throw new InvalidQuantityException("Quantity cannot be negative");
        }
        if (value > 100) {
            throw new InvalidQuantityException("Quantity cannot exceed 100");
        }
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public Quantity increment() {
        return new Quantity(value + 1);
    }

    public Quantity decrement() {
        if (value <= 1) {
            throw new InvalidQuantityException("Quantity cannot be less than 1");
        }
        return new Quantity(value - 1);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Quantity quantity = (Quantity) o;
        return value == quantity.value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }
}

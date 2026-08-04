package com.combat47.ecommerce.catalog.domain.model;

import com.combat47.ecommerce.catalog.domain.exception.InsufficientStockException;

import java.util.Objects;

public class Stock {

    private final int quantity;

    public Stock(int quantity) {
        if (quantity < 0) {
            throw new InsufficientStockException("Stock cannot be negative:  " + quantity);
        }
        this.quantity = quantity;
    }

    public int getQuantity() {
        return quantity;
    }

    public Stock increase(int amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Increase amount must be positive");
        }
        return new Stock(this.quantity + amount);
    }

    public Stock decrease(int amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Decrease amount must be positive");
        }
        if (this.quantity < amount) {
            throw new InsufficientStockException(
                    "Insufficient stock. Available: " + this.quantity + ", requested: " + amount
            );
        }
        return new Stock(this.quantity - amount);
    }

    public boolean hasEnough(int requested) {
        return this.quantity >= requested;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Stock stock = (Stock) o;
        return quantity == stock.quantity;
    }

    @Override
    public int hashCode() {
        return Objects.hash(quantity);
    }

    @Override
    public String toString() {
        return String.valueOf(quantity);
    }
}

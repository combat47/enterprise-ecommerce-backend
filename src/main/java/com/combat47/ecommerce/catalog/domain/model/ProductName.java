package com.combat47.ecommerce.catalog.domain.model;

import com.combat47.ecommerce.catalog.domain.exception.InvalidProductException;

import java.util.Objects;

public class ProductName {
    private final String value;


    public ProductName(String value) {
        if (value == null || value.isBlank()) {
            throw new InvalidProductException("Product name cannot be null or blank");
        }
        this.value = value.trim().replaceAll("\\s+", " ");
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProductName that = (ProductName) o;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return value;
    }
}

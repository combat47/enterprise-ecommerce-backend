package com.combat47.ecommerce.catalog.domain.model;

import com.combat47.ecommerce.catalog.domain.exception.InvalidSkuException;

import java.util.Objects;

public class Sku {
    private final String value;

    public Sku(String value) {
        if (value == null || value.isBlank()) {
            throw new InvalidSkuException("sku value cannot be null or blank");
        }
        this.value = value.trim().toUpperCase();
        if (this.value.length() < 3 || this.value.length() > 20) {
            throw new InvalidSkuException("SKU must be between 3 and 20 characters");
        }
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Sku sku = (Sku) o;
        return Objects.equals(value, sku.value);
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

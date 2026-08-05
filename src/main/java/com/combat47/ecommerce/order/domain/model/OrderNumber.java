package com.combat47.ecommerce.order.domain.model;

import java.util.Objects;
import java.util.UUID;

public class OrderNumber {
    private final String value;

    public OrderNumber(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Order number cannot be null or blank");
        }
        this.value = value;
    }

    public static OrderNumber generate() {
        return new OrderNumber("ORD-" + UUID.randomUUID().toString()
                .substring(0, 8).toUpperCase());
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderNumber that = (OrderNumber) o;
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

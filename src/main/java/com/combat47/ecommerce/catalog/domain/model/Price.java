package com.combat47.ecommerce.catalog.domain.model;

import com.combat47.ecommerce.catalog.domain.exception.InvalidPriceException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

public class Price {

    private final BigDecimal value;

    public Price(BigDecimal value) {
        if (value == null) {
            throw new InvalidPriceException("Price cannot be null");
        }
        if (value.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidPriceException("Price must be greater than zero");
        }
        this.value = value.setScale(2, RoundingMode.HALF_UP);
    }

    public BigDecimal getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Price price = (Price) o;
        return Objects.equals(value, price.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return value.toString();
    }
}

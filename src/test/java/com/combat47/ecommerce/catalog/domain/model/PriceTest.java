package com.combat47.ecommerce.catalog.domain.model;


import com.combat47.ecommerce.catalog.domain.exception.InvalidPriceException;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class PriceTest {

    @Test
    void should_create_valid_price() {
        Price price = new Price(new BigDecimal("99.99"));
        assertEquals(new BigDecimal("99.99"), price.getValue());
    }

    @Test
    void should_round_to_two_decimal_places() {
        Price price1 = new Price(new BigDecimal("99.995"));
        assertEquals(new BigDecimal("100.00"), price1.getValue());

        Price price2 = new Price(new BigDecimal("99.994"));
        assertEquals(new BigDecimal("99.99"), price2.getValue());
    }

    @Test
    void should_throw_when_price_is_null() {
        assertThrows(InvalidPriceException.class,
                () -> new Price(null));
    }

    @Test
    void should_throw_when_price_is_zero() {
        assertThrows(InvalidPriceException.class,
                () -> new Price(BigDecimal.ZERO));
    }

    @Test
    void should_throw_when_price_is_negative() {
        assertThrows(InvalidPriceException.class,
                () -> new Price(new BigDecimal("-10")));
    }

    @Test
    void should_be_equal_when_values_are_same() {
        Price price1 = new Price(new BigDecimal("99.99"));
        Price price2 = new Price(new BigDecimal("99.99"));
        assertEquals(price1, price2);
    }

    @Test
    void should_be_unequal_when_values_are_different() {
        Price price1 = new Price(new BigDecimal("99.99"));
        Price price2 = new Price(new BigDecimal("100.00"));
        assertNotEquals(price1, price2);
    }

    @Test
    void should_return_correct_hashcode() {
        Price price1 = new Price(new BigDecimal("99.99"));
        assertEquals(price1.hashCode(), new Price(new BigDecimal("99.99")).hashCode());
    }

    @Test
    void toString_should_return_formatted_value() {
        Price price1 = new Price(new BigDecimal("99.99"));
        assertEquals("99.99", price1.toString());
    }
}

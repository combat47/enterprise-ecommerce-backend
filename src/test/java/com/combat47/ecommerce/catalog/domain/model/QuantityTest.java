package com.combat47.ecommerce.catalog.domain.model;

import com.combat47.ecommerce.catalog.domain.exception.InvalidQuantityException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class QuantityTest {

    @Test
    void should_create_quantity() {
        Quantity q = new Quantity(5);
        assertEquals(5, q.getValue());
    }

    @Test
    void should_throw_when_negative() {
        assertThrows(InvalidQuantityException.class,
                () ->  new Quantity(-1));
    }

    @Test
    void should_throw_when_exceed_max() {
        assertThrows(InvalidQuantityException.class,
                () ->   new Quantity(101));
    }

    @Test
    void should_increment() {
        Quantity q = new Quantity(5);
        Quantity incremented = q.increment();
        assertEquals(6, incremented.getValue());
    }

    @Test
    void should_decrement() {
        Quantity q = new Quantity(5);
        Quantity decremented = q.decrement();
        assertEquals(4, decremented.getValue());
    }

    @Test
    void should_throw_when_decrement_below_1() {
        Quantity q = new Quantity(1);
        assertThrows(InvalidQuantityException.class, q::decrement);
    }
}

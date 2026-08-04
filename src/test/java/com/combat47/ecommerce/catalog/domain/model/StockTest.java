package com.combat47.ecommerce.catalog.domain.model;

import com.combat47.ecommerce.catalog.domain.exception.InsufficientStockException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StockTest {

    @Test
    void should_create_stock() {
        Stock stock = new Stock(10);
        assertEquals(10, stock.getQuantity());
    }

    @Test
    void should_throw_when_negative() {
        assertThrows(InsufficientStockException.class, () -> new Stock(-5));
    }

    @Test
    void should_increase() {
        Stock stock = new Stock(10);
        Stock increased = stock.increase(5);
        assertEquals(15, increased.getQuantity());
        assertEquals(10, stock.getQuantity());
    }

    @Test
    void should_throw_when_increase_with_negative_or_zero() {
        Stock stock = new Stock(10);
        assertThrows(IllegalArgumentException.class, () -> stock.increase(0));
        assertThrows(IllegalArgumentException.class, () -> stock.increase(-3));
    }

    @Test
    void should_decrease() {
        Stock stock = new Stock(10);
        Stock decreased = stock.decrease(3);
        assertEquals(7, decreased.getQuantity());
        assertEquals(10, stock.getQuantity());
    }

    @Test
    void should_throw_when_decrease_below_zero() {
        Stock stock = new Stock(10);
        assertThrows(InsufficientStockException.class, () -> stock.decrease(15));
    }

    @Test
    void should_throw_when_decrease_with_negative_or_zero() {
        Stock stock = new Stock(10);
        assertThrows(IllegalArgumentException.class, () -> stock.decrease(0));
        assertThrows(IllegalArgumentException.class, () -> stock.decrease(-3));
    }

    @Test
    void hasEnough_should_return_true_when_enough() {
        Stock stock = new Stock(10);
        assertTrue(stock.hasEnough(5));
        assertTrue(stock.hasEnough(10));
    }

    @Test
    void hasEnough_should_return_false_when_not_enough() {
        Stock stock = new Stock(10);
        assertFalse(stock.hasEnough(11));
    }

    @Test
    void should_be_equal_when_same_quantity() {
        Stock stock1 = new Stock(10);
        Stock stock2 = new Stock(10);
        assertEquals(stock1, stock2);
        assertEquals(stock1.hashCode(), stock2.hashCode());
    }

    @Test
    void toString_should_return_quantity() {
        Stock stock = new Stock(42);
        assertEquals("42", stock.toString());
    }
}
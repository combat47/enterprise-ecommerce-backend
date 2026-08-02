package com.combat47.ecommerce.catalog.domain.model;

import com.combat47.ecommerce.catalog.domain.exception.InvalidProductException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ProductNameTest {

    @Test
    void should_create_valid_product_name() {
        ProductName name = new ProductName("Laptop");
        assertEquals("Laptop", name.getValue());
    }

    @Test
    void should_trim_and_normalize_spaces() {
        ProductName name = new ProductName("  Laptop   Pro  ");
        assertEquals("Laptop Pro", name.getValue());
    }

    @Test
    void should_throw_when_name_is_null() {
        assertThrows(InvalidProductException.class,
                () -> new ProductName(null));
    }

    @Test
    void should_throw_when_name_is_blank() {
        assertThrows(InvalidProductException.class,
                () -> new ProductName("   "));
        assertThrows(InvalidProductException.class,
                () ->  new ProductName(""));
    }

    @Test
    void should_be_unequal_when_values_are_different() {
        ProductName name1 = new ProductName("Laptop");
        ProductName name2 = new ProductName("Desktop");
        assertNotEquals(name1, name2);
    }

    @Test
    void should_return_correct_hashCode() {
        ProductName name = new ProductName("Laptop");
        assertEquals(name.hashCode(), new ProductName("Laptop").hashCode());
    }

    @Test
    void toString_should_return_value() {
        ProductName name = new ProductName("Laptop");
        assertEquals("Laptop", name.toString());
    }
}

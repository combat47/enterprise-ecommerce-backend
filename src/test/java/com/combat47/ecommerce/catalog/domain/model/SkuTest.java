package com.combat47.ecommerce.catalog.domain.model;

import com.combat47.ecommerce.catalog.domain.exception.InvalidSkuException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SkuTest {

    @Test
    void should_create_valid_sku() {
        Sku sku = new Sku("SKU-001");
        assertEquals("SKU-001", sku.getValue());
    }

    @Test
    void should_convert_to_uppercase_and_trim() {
        Sku sku = new Sku("SKU-001");
        assertEquals("SKU-001", sku.getValue());
    }

    @Test
    void should_throw_when_sku_is_null() {
        assertThrows(InvalidSkuException.class,
                () -> new Sku(null));
    }

    @Test
    void should_throw_when_sku_is_blank() {
        assertThrows(InvalidSkuException.class,
                () -> new Sku(""));
        assertThrows(InvalidSkuException.class,
                () ->  new Sku("   "));
    }

    @Test
    void should_throw_when_sku_is_too_short() {
        assertThrows(InvalidSkuException.class,
                () -> new Sku("A1"));
    }

    @Test
    void should_throw_when_sku_is_too_long() {
        assertThrows(InvalidSkuException.class,
                () -> new Sku("A".repeat(21)));
    }

    @Test
    void should_be_equal_when_values_are_same() {
        Sku sku1 = new Sku("SKU-001");
        Sku sku2 = new Sku("SKU-001");
        assertEquals(sku1, sku2);
    }

    @Test
    void should_return_correct_hashcode() {
        Sku sku1 = new Sku("SKU-001");
        assertEquals(sku1.hashCode(), new Sku("SKU-001").hashCode());
    }

    @Test
    void toString_should_return_value() {
        Sku sku = new Sku("SKU-001");
        assertEquals("SKU-001", sku.toString());
    }
}

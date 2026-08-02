package com.combat47.ecommerce.catalog.domain.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DescriptionTest {

    @Test
    void should_create_valid_description() {
        Description desc = new Description("This is a product description");
        assertEquals("This is a product description", desc.getValue());
    }

    @Test
    void should_trim_description() {
        Description desc = new Description("  description with spaces  ");
        assertEquals("description with spaces", desc.getValue());
    }

    @Test
    void should_allow_null_values() {
        Description desc = new Description(null);
        assertNull(desc.getValue());
        assertEquals("", desc.toString());
    }

    @Test
    void should_allow_empty_description() {
        Description desc = new Description("");
        assertEquals("", desc.getValue());
    }

    @Test
    void should_be_equal_when_are_same() {
        Description desc1 = new Description("description");
        Description desc2 = new Description("description");
        assertEquals(desc1, desc2);
    }

    @Test
    void should_be_unequal_when_values_are_different() {
        Description desc1 = new Description("desc1");
        Description desc2 = new Description("desc2");
        assertNotEquals(desc1, desc2);
    }

    @Test
    void should_return_correct_hashcode() {
        Description desc =  new Description("description");
        assertEquals(desc.hashCode(), new Description("description").hashCode());
    }

    @Test
    void toString_should_return_value_or_empty() {
        assertEquals("Description", new Description("Description").toString());
        assertEquals("", new Description(null).toString());
        assertEquals("", new Description("").toString());
    }
}

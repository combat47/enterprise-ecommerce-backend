package com.combat47.ecommerce.catalog.domain.model;

import com.combat47.ecommerce.catalog.domain.exception.InvalidQuantityException;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class CartItemTest {

    private static final UUID PRODUCT_ID = UUID.randomUUID();
    private static final Price PRICE = new Price(new BigDecimal("99.99"));
    private static final Quantity QUANTITY = new Quantity(2);

    @Test
    void should_create_cart_item() {
        CartItem item = CartItem.create(PRODUCT_ID, "Laptop", PRICE, QUANTITY);

        assertNotNull(item.getId());
        assertEquals(PRODUCT_ID, item.getProductId());
        assertEquals("Laptop", item.getProductName());
        assertEquals(PRICE, item.getPrice());
        assertEquals(QUANTITY, item.getQuantity());
        assertEquals(new BigDecimal("199.98"), item.totalPrice());
    }

    @Test
    void should_increase_quantity() {
        CartItem item = CartItem.create(PRODUCT_ID, "Laptop", PRICE, new Quantity(2));
        item.increaseQuantity();
        assertEquals(3, item.getQuantity().getValue());
        assertEquals(new BigDecimal("299.97"), item.totalPrice());
    }

    @Test
    void should_decrease_quantity() {
        CartItem item = CartItem.create(PRODUCT_ID, "Laptop", PRICE, new Quantity(2));
        item.decreaseQuantity();
        assertEquals(1, item.getQuantity().getValue());
        assertEquals(new BigDecimal("99.99"), item.totalPrice());
    }

    @Test
    void should_throw_when_decrease_below_1() {
        CartItem item = CartItem.create(PRODUCT_ID, "Laptop", PRICE, new Quantity(1));
        assertThrows(InvalidQuantityException.class, item::decreaseQuantity);
    }

    @Test
    void should_change_quantity() {
        CartItem item = CartItem.create(PRODUCT_ID, "Laptop", PRICE, new Quantity(2));
        item.changeQuantity(new Quantity(5));
        assertEquals(5, item.getQuantity().getValue());
        assertEquals(new BigDecimal("499.95"), item.totalPrice());
    }

    @Test
    void items_with_same_id_should_be_equal() {
        UUID id = UUID.randomUUID();
        CartItem item1 = new CartItem(id, PRODUCT_ID, "Laptop", PRICE, QUANTITY);
        CartItem item2 = new CartItem(id, PRODUCT_ID, "Laptop", PRICE, QUANTITY);
        assertEquals(item1, item2);
    }
}
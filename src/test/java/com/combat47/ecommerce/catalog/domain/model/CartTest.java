package com.combat47.ecommerce.catalog.domain.model;

import com.combat47.ecommerce.catalog.domain.exception.CartEmptyException;
import com.combat47.ecommerce.catalog.domain.exception.CartItemNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class CartTest {

    private static final UUID USER_ID = UUID.randomUUID();
    private static final UUID PRODUCT_ID_1 = UUID.randomUUID();
    private static final UUID PRODUCT_ID_2 = UUID.randomUUID();

    private Cart cart;
    private CartItem item1;
    private CartItem item2;

    @BeforeEach
    void setUp() {
        cart = Cart.create(USER_ID);
        item1 = CartItem.create(PRODUCT_ID_1, "Product 1", new Price(new BigDecimal("10.00")), new Quantity(2));
        item2 = CartItem.create(PRODUCT_ID_2, "Product 2", new Price(new BigDecimal("20.00")), new Quantity(1));
    }

    @Test
    void should_create_cart() {
        assertNotNull(cart.getId());
        assertEquals(USER_ID, cart.getUserId());
        assertEquals(0, cart.itemCount());
        assertEquals(BigDecimal.ZERO, cart.calculateTotal());
        assertNotNull(cart.getCreatedAt());
        assertNotNull(cart.getUpdatedAt());
        assertEquals(cart.getCreatedAt(), cart.getUpdatedAt());
    }

    @Test
    void should_add_item() {
        cart.addItem(item1);
        assertEquals(1, cart.itemCount());
        assertEquals(new BigDecimal("20.00"), cart.calculateTotal());
    }

    @Test
    void should_increase_quantity_when_adding_same_product() {
        cart.addItem(item1);
        cart.addItem(item1);
        assertEquals(1, cart.itemCount());
        assertEquals(3, cart.getItems().iterator().next().getQuantity().getValue());
        assertEquals(new BigDecimal("30.00"), cart.calculateTotal());
    }

    @Test
    void should_remove_item() {
        cart.addItem(item1);
        cart.addItem(item2);
        cart.removeItem(PRODUCT_ID_1);
        assertEquals(1, cart.itemCount());
        assertEquals(new BigDecimal("20.00"), cart.calculateTotal());
    }

    @Test
    void should_throw_when_removing_non_existent_item() {
        assertThrows(CartItemNotFoundException.class, () -> cart.removeItem(PRODUCT_ID_1));
    }

    @Test
    void should_update_quantity() {
        cart.addItem(item1);
        cart.updateQuantity(PRODUCT_ID_1, new Quantity(5));
        assertEquals(5, cart.getItems().iterator().next().getQuantity().getValue());
        assertEquals(new BigDecimal("50.00"), cart.calculateTotal());
    }

    @Test
    void should_throw_when_updating_non_existent_item() {
        assertThrows(CartItemNotFoundException.class, () -> cart.updateQuantity(PRODUCT_ID_1, new Quantity(5)));
    }

    @Test
    void should_clear_cart() {
        cart.addItem(item1);
        cart.addItem(item2);
        cart.clear();
        assertEquals(0, cart.itemCount());
        assertEquals(BigDecimal.ZERO, cart.calculateTotal());
    }

    @Test
    void should_throw_when_clearing_empty_cart() {
        assertThrows(CartEmptyException.class, cart::clear);
    }

    @Test
    void should_update_timestamp_on_change() throws InterruptedException {
        cart.addItem(item1);
        Instant old = cart.getUpdatedAt();
        Thread.sleep(10);
        cart.addItem(item2);
        assertTrue(cart.getUpdatedAt().isAfter(old));
    }

    @Test
    void carts_with_same_id_should_be_equal() {
        UUID id = UUID.randomUUID();
        Cart c1 = Cart.restore(id, USER_ID, null, Instant.now(), Instant.now());
        Cart c2 = Cart.restore(id, USER_ID, null, Instant.now(), Instant.now());
        assertEquals(c1, c2);
    }
}
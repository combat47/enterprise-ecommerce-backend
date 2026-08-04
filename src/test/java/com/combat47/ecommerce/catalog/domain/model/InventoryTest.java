package com.combat47.ecommerce.catalog.domain.model;

import com.combat47.ecommerce.catalog.domain.exception.InsufficientStockException;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class InventoryTest {

    private static final UUID PRODUCT_ID = UUID.randomUUID();
    private static final Stock INITIAL_STOCK = new Stock(10);

    @Test
    void should_create_inventory() {
        Inventory inventory = Inventory.create(PRODUCT_ID, INITIAL_STOCK);

        assertNotNull(inventory.getId());
        assertEquals(PRODUCT_ID, inventory.getProductId());
        assertEquals(INITIAL_STOCK, inventory.getStock());
        assertEquals(10, inventory.availableQuantity());
        assertNotNull(inventory.getCreatedAt());
        assertNotNull(inventory.getUpdatedAt());
        assertEquals(inventory.getCreatedAt(), inventory.getUpdatedAt());
    }

    @Test
    void should_restore_inventory() {
        UUID id = UUID.randomUUID();
        Instant now = Instant.now();

        Inventory restored = Inventory.restore(id, PRODUCT_ID, INITIAL_STOCK, now, now);

        assertEquals(id, restored.getId());
        assertEquals(PRODUCT_ID, restored.getProductId());
        assertEquals(INITIAL_STOCK, restored.getStock());
        assertEquals(10, restored.availableQuantity());
        assertEquals(now, restored.getCreatedAt());
        assertEquals(now, restored.getUpdatedAt());
    }

    @Test
    void should_increase_stock() throws InterruptedException {
        Inventory inventory = Inventory.create(PRODUCT_ID, INITIAL_STOCK);
        Instant oldUpdatedAt = inventory.getUpdatedAt();

        Thread.sleep(10);

        inventory.increaseStock(5);

        assertEquals(15, inventory.availableQuantity());
        assertEquals(new Stock(15), inventory.getStock());
        assertTrue(inventory.getUpdatedAt().isAfter(oldUpdatedAt));
    }

    @Test
    void should_decrease_stock() throws InterruptedException {
        Inventory inventory = Inventory.create(PRODUCT_ID, INITIAL_STOCK);
        Instant oldUpdatedAt = inventory.getUpdatedAt();

        Thread.sleep(10);

        inventory.decreaseStock(3);

        assertEquals(7, inventory.availableQuantity());
        assertEquals(new Stock(7), inventory.getStock());
        assertTrue(inventory.getUpdatedAt().isAfter(oldUpdatedAt));
    }

    @Test
    void should_throw_when_decrease_below_zero() {
        Inventory inventory = Inventory.create(PRODUCT_ID, INITIAL_STOCK);

        assertThrows(InsufficientStockException.class, () -> inventory.decreaseStock(15));
    }

    @Test
    void should_throw_when_decrease_with_negative_or_zero() {
        Inventory inventory = Inventory.create(PRODUCT_ID, INITIAL_STOCK);

        assertThrows(IllegalArgumentException.class, () -> inventory.decreaseStock(0));
        assertThrows(IllegalArgumentException.class, () -> inventory.decreaseStock(-5));
    }

    @Test
    void hasEnough_should_return_true_when_enough() {
        Inventory inventory = Inventory.create(PRODUCT_ID, INITIAL_STOCK);
        assertTrue(inventory.hasEnough(5));
        assertTrue(inventory.hasEnough(10));
    }

    @Test
    void hasEnough_should_return_false_when_not_enough() {
        Inventory inventory = Inventory.create(PRODUCT_ID, INITIAL_STOCK);
        assertFalse(inventory.hasEnough(11));
    }

    @Test
    void should_not_update_updatedAt_when_stock_not_changed() {
        Instant now = Instant.now();
        Inventory inventory = Inventory.restore(
                UUID.randomUUID(),
                PRODUCT_ID,
                INITIAL_STOCK,
                now,
                now
        );
        assertEquals(now, inventory.getCreatedAt());
        assertEquals(now, inventory.getUpdatedAt());
    }

    @Test
    void inventories_with_same_id_should_be_equal() {
        UUID id = UUID.randomUUID();
        Instant now = Instant.now();

        Inventory inv1 = Inventory.restore(id, PRODUCT_ID, INITIAL_STOCK, now, now);
        Inventory inv2 = Inventory.restore(id, PRODUCT_ID, INITIAL_STOCK, now, now);

        assertEquals(inv1, inv2);
        assertEquals(inv1.hashCode(), inv2.hashCode());
    }
}
package com.combat47.ecommerce.catalog.domain.model;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ProductTest {

    private static final ProductName NAME = new ProductName("Laptop");
    private static final Description DESCRIPTION = new Description("Powerful laptop");

    private static final Price PRICE = new Price(new BigDecimal("999.99"));
    private static final Sku SKU = new Sku("LAP-001");

    @Test
    void should_create_product() {
        Product product = Product.create(NAME, DESCRIPTION, PRICE, SKU);

        assertNotNull(product.getId());
        assertEquals(NAME, product.getName());
        assertEquals(DESCRIPTION, product.getDescription());
        assertEquals(PRICE, product.getPrice());
        assertEquals(SKU, product.getSku());
        assertTrue(product.isActive());
        assertNotNull(product.getCreatedAt());
        assertNotNull(product.getUpdatedAt());
        assertEquals(product.getCreatedAt(), product.getUpdatedAt());
    }

    @Test
    void should_restore_product() {
        UUID id = UUID.randomUUID();
        Instant now = Instant.now();

        Product product = Product.restore(
                id,
                NAME,
                DESCRIPTION,
                PRICE,
                SKU,
                true,
                now,
                now
        );

        assertEquals(id, product.getId());
        assertEquals(NAME, product.getName());
        assertEquals(DESCRIPTION, product.getDescription());
        assertEquals(PRICE, product.getPrice());
        assertEquals(SKU, product.getSku());
        assertTrue(product.isActive());
        assertEquals(now, product.getCreatedAt());
        assertEquals(now, product.getUpdatedAt());
    }

    @Test
    void should_update_product() {
        Product product = Product.create(NAME, DESCRIPTION, PRICE, SKU);

        ProductName newName = new ProductName("Gaming Laptop");
        Description newDesc = new Description("High-end gaming laptop");
        Price newPrice = new Price(new BigDecimal("1499.99"));
        Sku newSKU = new Sku("GAM-001");

        Instant oldUpdateAt = product.getUpdatedAt();

        try { Thread.sleep((10)); } catch (InterruptedException ignored) {}

        product.update(newName, newDesc, newPrice, newSKU);

        assertEquals(newName, product.getName());
        assertEquals(newDesc, product.getDescription());
        assertEquals(newPrice, product.getPrice());
        assertEquals(newSKU, product.getSku());
        assertNotEquals(oldUpdateAt, product.getUpdatedAt());
        assertTrue(product.isActive());
    }

    @Test
    void should_activate_product() {
        Product product = Product.create(NAME, DESCRIPTION, PRICE, SKU);
        product.deactivate();
        assertFalse(product.isActive());

        try { Thread.sleep(10); } catch (InterruptedException ignored) {}

        Instant oldUpdateAt = product.getUpdatedAt();

        product.activate();

        assertTrue(product.isActive());
        assertNotEquals(oldUpdateAt, product.getUpdatedAt());
    }

    @Test
    void should_deactivate_product() {
        Product product = Product.create(NAME, DESCRIPTION, PRICE, SKU);
        assertTrue(product.isActive());

        Instant oldUpdateAt = product.getUpdatedAt();

        try { Thread.sleep(10); } catch (InterruptedException ignored) {}

        product.deactivate();

        assertFalse(product.isActive());
        assertNotEquals(oldUpdateAt, product.getUpdatedAt());
    }

    @Test
    void should_not_update_updateAt_when_activating_already_active() {
        Product product = Product.create(NAME, DESCRIPTION, PRICE, SKU);
        assertTrue(product.isActive());

        Instant oldUpdateAt = product.getUpdatedAt();

        product.activate();

        assertEquals(oldUpdateAt, product.getUpdatedAt());
        assertTrue(product.isActive());

    }

    @Test
    void should_not_update_updateAt_when_deactivating_already_inactive() {
        Product product = Product.create(NAME, DESCRIPTION, PRICE, SKU);
        product.deactivate();
        assertFalse(product.isActive());

        Instant oldUpdateAt = product.getUpdatedAt();

        product.deactivate();

        assertEquals(oldUpdateAt, product.getUpdatedAt());
        assertFalse(product.isActive());
    }

    @Test
    void product_with_same_id_should_be_equal() {
        UUID id = UUID.randomUUID();
        Instant now = Instant.now();

        Product product1 = Product.restore(id, NAME, DESCRIPTION, PRICE, SKU, true,now,now);
        Product product2 = Product.restore(id, NAME, DESCRIPTION, PRICE, SKU, false,now,now);

        assertEquals(product1, product2);
        assertEquals(product1.hashCode(), product2.hashCode());
    }

    @Test
    void product_with_different_id_should_not_be_equal() {
        Instant now = Instant.now();

        Product product1 = Product.restore(UUID.randomUUID(), NAME, DESCRIPTION, PRICE, SKU, true,now,now);
        Product product2 = Product.restore(UUID.randomUUID(), NAME, DESCRIPTION, PRICE, SKU, true,now,now);

        assertNotEquals(product1, product2);
    }

}

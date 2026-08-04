package com.combat47.ecommerce.catalog.domain.model;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

public class Inventory {

    private final UUID id;
    private final UUID productId;
    private Stock stock;
    private final Instant createdAt;
    private Instant updatedAt;

    public Inventory(UUID id, UUID productId, Stock stock, Instant createdAt, Instant updatedAt) {
        this.id = id;
        this.productId = productId;
        this.stock = stock;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static Inventory create(UUID productId, Stock initialStock) {
        Instant now = Instant.now();
        return new Inventory(
                UUID.randomUUID(),
                productId,
                initialStock,
                now,
                now);
    }

    public static Inventory restore(UUID id, UUID productId, Stock stock, Instant createdAt, Instant updatedAt) {
        return new Inventory(id, productId, stock, createdAt, updatedAt);
    }

    public void increaseStock(int amount) {
        this.stock = this.stock.increase(amount);
        this.updatedAt = Instant.now();
    }

    public void decreaseStock(int amount) {
        this.stock = this.stock.decrease(amount);
        this.updatedAt = Instant.now();
    }

    public boolean hasEnough(int requested) {
        return this.stock.hasEnough(requested);
    }

    public int availableQuantity() {
        return this.stock.getQuantity();
    }

    public UUID getId() {
        return id;
    }

    public UUID getProductId() {
        return productId;
    }

    public Stock getStock() {
        return stock;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Inventory inventory = (Inventory) o;
        return Objects.equals(id, inventory.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Inventory{" +
                "id=" + id +
                ", productId=" + productId +
                ", stock=" + stock +
                "}";
    }
}

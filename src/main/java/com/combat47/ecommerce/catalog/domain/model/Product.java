package com.combat47.ecommerce.catalog.domain.model;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

public class Product {

    private final UUID id;
    private ProductName name;
    private Description description;
    private Price price;
    private Sku sku;
    private boolean active;
    private final Instant createdAt;
    private Instant updatedAt;

    public Product(
            UUID id,
            ProductName name,
            Description description,
            Price price,
            Sku sku,
            boolean active,
            Instant createdAt,
            Instant updatedAt
            ) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.sku = sku;
        this.active = active;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static Product create(ProductName name, Description description, Price price, Sku sku) {
        Instant now = Instant.now();
        return new Product(UUID.randomUUID(), name, description, price, sku, true, now, now);
    }

    public static Product restore(UUID id, ProductName name, Description description, Price price, Sku sku, boolean active, Instant createdAt, Instant updatedAt) {
        return new Product(id, name, description, price, sku, active, createdAt, updatedAt );
    }

    public void update(ProductName name, Description description, Price price, Sku sku) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.sku = sku;
        this.updatedAt = Instant.now();
    }

    public void activate() {
        if (!this.active) {
            this.active = true;
            this.updatedAt = Instant.now();
        }
    }

    public void deactivate() {
        if (this.active) {
            this.active = false;
            this.updatedAt = Instant.now();
        }
    }

    public UUID getId() {
        return id;
    }

    public ProductName getName() {
        return name;
    }

    public Description getDescription() {
        return description;
    }

    public Price getPrice() {
        return price;
    }

    public Sku getSku() {
        return sku;
    }

    public boolean isActive() {
        return active;
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
        Product product = (Product) o;
        return Objects.equals(id, product.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name=" + name +
                ", sku=" + sku +
                ", active=" + active +
                "}";
    }
}

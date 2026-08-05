package com.combat47.ecommerce.order.domain.model;

import java.util.Objects;
import java.util.UUID;

public class OrderItem {
    private final UUID id;
    private final UUID productId;
    private final String productName;
    private final Money unitPrice;
    private final int quantity;
    private final Money totalPrice;

    public OrderItem(UUID id, UUID productId, String productName, Money unitPrice, int quantity, Money totalPrice) {
        this.id = id;
        this.productId = productId;
        this.productName = productName;
        this.unitPrice = unitPrice;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
    }

    public static OrderItem create(UUID productId, String productName, Money unitPrice, int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be positive");
        }
        Money total = unitPrice.multiply(quantity);
        return new OrderItem(UUID.randomUUID(), productId, productName, unitPrice, quantity, total);
    }

    public static OrderItem restore(UUID id, UUID productId, String productName, Money unitPrice, int quantity, Money totalPrice) {
        return new OrderItem(id, productId, productName, unitPrice, quantity, totalPrice);
    }

    public UUID getId() {
        return id;
    }

    public UUID getProductId() {
        return productId;
    }

    public String getProductName() {
        return productName;
    }

    public Money getUnitPrice() {
        return unitPrice;
    }

    public int getQuantity() {
        return quantity;
    }

    public Money getTotalPrice() {
        return totalPrice;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderItem orderItem = (OrderItem) o;
        return Objects.equals(id, orderItem.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}

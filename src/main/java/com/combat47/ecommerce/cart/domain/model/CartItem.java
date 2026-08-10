package com.combat47.ecommerce.cart.domain.model;

import com.combat47.ecommerce.cart.domain.exception.InvalidQuantityException;
import com.combat47.ecommerce.order.domain.model.Money;

import java.util.Objects;
import java.util.UUID;

public class CartItem {

    private final UUID id;
    private final UUID productId;
    private final String productName;
    private final Money unitPrice;
    private int quantity;

    private CartItem(UUID id, UUID productId, String productName, Money unitPrice, int quantity) {
        this.id = id;
        this.productId = productId;
        this.productName = productName;
        this.unitPrice = unitPrice;
        this.quantity = quantity;
    }

    public static CartItem create(UUID productId, String productName, Money unitPrice, int quantity) {
        if (quantity <= 0) {
            throw new InvalidQuantityException("Quantity must be greater than zero");
        }
        return new CartItem(UUID.randomUUID(), productId, productName, unitPrice, quantity);
    }

    public static CartItem restore(UUID id, UUID productId, String productName, Money unitPrice, int quantity) {
        return new CartItem(id, productId, productName, unitPrice, quantity);
    }

    // ===== Business Methods =====

    public void increase(int amount) {
        if (amount <= 0) {
            throw new InvalidQuantityException("Increase amount must be positive");
        }
        this.quantity += amount;
    }

    public void decrease() {
        if (this.quantity <= 1) {
            throw new InvalidQuantityException("Quantity cannot be less than 1");
        }
        this.quantity--;
    }

    public void changeQuantity(int newQuantity) {
        if (newQuantity <= 0) {
            throw new InvalidQuantityException("Quantity must be greater than zero");
        }
        this.quantity = newQuantity;
    }

    public Money subtotal() {
        return unitPrice.multiply(quantity);
    }

    // ===== Getters =====

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CartItem cartItem = (CartItem) o;
        return Objects.equals(id, cartItem.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "CartItem{" +
                "id=" + id +
                ", productId=" + productId +
                ", productName='" + productName + '\'' +
                ", quantity=" + quantity +
                '}';
    }
}
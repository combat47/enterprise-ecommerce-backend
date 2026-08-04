package com.combat47.ecommerce.catalog.domain.model;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;

public class CartItem {

    private final UUID id;
    private final UUID productId;
    private final String productName;
    private final Price price;
    private Quantity quantity;

    public CartItem(UUID id, UUID productId, String productName, Price price, Quantity quantity) {
        this.id = id;
        this.productId = productId;
        this.productName = productName;
        this.price = price;
        this.quantity = quantity;


    }

    public static CartItem create(UUID productId, String productName, Price price, Quantity quantity) {
        return new CartItem(UUID.randomUUID(), productId, productName, price, quantity);
    }

    public void increaseQuantity() {
        this.quantity = this.quantity.increment();
    }

    public void decreaseQuantity() {
        this.quantity = this.quantity.decrement();
    }

    public Quantity changeQuantity(Quantity newQuantity) {
        return this.quantity = newQuantity;
    }

    public BigDecimal totalPrice() {
        return price.getValue().multiply(BigDecimal.valueOf(quantity.getValue()));
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

    public Price getPrice() {
        return price;
    }

    public Quantity getQuantity() {
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
}

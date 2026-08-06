package com.combat47.ecommerce.cart.domain.model;

import com.combat47.ecommerce.cart.domain.exception.CartNotFoundException;
import com.combat47.ecommerce.cart.domain.exception.InvalidQuantityException;
import com.combat47.ecommerce.catalog.domain.exception.CartItemNotFoundException;
import com.combat47.ecommerce.order.domain.model.Money;

import java.time.Instant;
import java.util.*;

import static org.hibernate.validator.internal.util.Version.touch;

public class Cart {

    private final UUID id;
    private final UUID costumerId;
    private final List<CartItem> items;
    private final Instant createdAt;
    private Instant updatedAt;

    public Cart(UUID id, UUID costumerId, List<CartItem> items,
                Instant createdAt, Instant updatedAt) {
        this.id = id;
        this.costumerId = costumerId;
        this.items = new ArrayList<>(items != null ? items : Collections.emptyList());
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static Cart create(UUID customerId) {
        Instant now = Instant.now();
        return new Cart(UUID.randomUUID(), customerId, new ArrayList<>(), now, now);
    }

    public static Cart restore(UUID id,  UUID costumerId, List<CartItem> items,
                               Instant createdAt, Instant updatedAt) {
        return new Cart(id, costumerId, items, createdAt, updatedAt);
    }

    // Business Methods

    public void addProduct(UUID productId, String productName, Money unitPrice, int quantity) {
        if (quantity < 0) {
            throw new InvalidQuantityException("Quantity must be positive");
        }
        if (quantity == 0) {
            throw new InvalidQuantityException("Quantity must be greater than zero");
        }

        for (CartItem item : items) {
            if (item.getProductId().equals(productId)) {
                item.increase(quantity);
                this.updatedAt = Instant.now();
                return;
            }
        }

        CartItem newItem = CartItem.create(productId, productName, unitPrice, quantity);
        items.add(newItem);
        this.updatedAt = Instant.now();
    }

    public void removeProduct(UUID productId) {
        boolean removed = items.removeIf(item -> item.getProductId().equals(productId));

        if (!removed) {
            throw new CartItemNotFoundException("Product not found in cart: " + productId);
        }
        touch();
    }

    public void changeQuantity(UUID productId, int newQuantity) {
        if (newQuantity < 0) {
            throw new InvalidQuantityException("Quantity cannot be negative");
        }

        CartItem item = items.stream()
                .filter(i -> i.getProductId().equals(productId))
                .findFirst()
                .orElseThrow(() -> new CartItemNotFoundException("Product not found in cart: " + productId));

        if (newQuantity == 0) {
            removeProduct(productId);
            return;
        }

        item.changeQuantity(newQuantity);
        touch();
    }

    public void clear() {
        if (items.isEmpty()) {
            return;
        }
        items.clear();
        this.updatedAt = Instant.now();
    }

    public Money calculateTotalPrice() {
        return items.stream()
                .map(CartItem::subtotal)
                .reduce(new Money(java.math.BigDecimal.ZERO), Money::add);
    }

    public boolean isEmpty() {
        return items.isEmpty();
    }

    public int itemCount() {
        return items.size();
    }

    public int totalQuantity() {
        return items.stream().mapToInt(CartItem::getQuantity).sum();
    }

    public UUID getId() {
        return id;
    }

    public UUID getCostumerId() {
        return costumerId;
    }

    public List<CartItem> getItems() {
        return items;
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
        Cart cart = (Cart) o;
        return Objects.equals(id, cart.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}

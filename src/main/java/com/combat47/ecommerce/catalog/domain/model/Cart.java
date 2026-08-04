package com.combat47.ecommerce.catalog.domain.model;

import com.combat47.ecommerce.catalog.domain.exception.CartEmptyException;
import com.combat47.ecommerce.catalog.domain.exception.CartItemNotFoundException;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.*;

public class Cart {

    private final UUID id;
    private final UUID userId;
    private final Set<CartItem> items;
    private final Instant createdAt;
    private Instant updatedAt;

    public Cart(UUID id, UUID userId, Set<CartItem> items, Instant createdAt, Instant updatedAt) {
        this.id = id;
        this.userId = userId;
        this.items = new HashSet<>();
        if (items != null) {
            this.items.addAll(items);
        }
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static Cart create(UUID userId) {
        Instant now = Instant.now();
        return new Cart(UUID.randomUUID(), userId, new HashSet<>(), now, now);
    }

    public static Cart restore(UUID id, UUID userId, Set<CartItem> items, Instant createdAt, Instant updatedAt) {
        return new Cart(id, userId, items, createdAt, updatedAt);
    }

    public void addItem(CartItem newItem) {
        Optional<CartItem> existing = this.items.stream()
                .filter(item -> item.getProductId().equals(newItem.getProductId()))
                .findFirst();
        if (existing.isPresent()) {
            existing.get().increaseQuantity();
        } else {
            items.add(newItem);
        }
        this.updatedAt = Instant.now();
    }

    public void removeItem(UUID productId) {
        boolean removed = this.items.removeIf(item -> item.getProductId().equals(productId));
        if (!removed) {
            throw new CartItemNotFoundException("Product not found in cart: " + productId);
        }
        this.updatedAt = Instant.now();
    }

    public void updateQuantity(UUID productId, Quantity newQuantity) {
        CartItem item = items.stream().
                filter(i -> i.getProductId().equals(productId))
                .findFirst()
                .orElseThrow(() -> new CartItemNotFoundException("Product not found in cart: " + productId));
        item.changeQuantity(newQuantity);
        this.updatedAt = Instant.now();
    }

    public void clear() {
        if (items.isEmpty()) {
            throw new CartEmptyException("Cart is already empty");
        }
        items.clear();
        this.updatedAt = Instant.now();
    }

    public BigDecimal calculateTotal() {
        return items.stream()
                .map(CartItem::totalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public int itemCount() {
        return items.size();
    }

    public UUID getId() {
        return id;
    }

    public UUID getUserId() {
        return userId;
    }

    public Set<CartItem> getItems() {
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

package com.combat47.ecommerce.order.domain.model;

import com.combat47.ecommerce.order.domain.exception.InvalidOrderStatusException;
import com.combat47.ecommerce.order.domain.exception.OrderCancellationException;

import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class Order {
    private final UUID id;
    private final OrderNumber orderNumber;
    private final UUID userId;
    private final List<OrderItem> items;
    private OrderStatus status;
    private final Money totalAmount;
    private final Instant createdAt;
    private Instant updatedAt;

    public Order(UUID id, OrderNumber orderNumber, UUID userId, List<OrderItem> items,
                 OrderStatus status, Money totalAmount, Instant createdAt, Instant updatedAt) {
        this.id = id;
        this.orderNumber = orderNumber;
        this.userId = userId;
        this.items = items;
        this.status = status;
        this.totalAmount = totalAmount;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static Order create(UUID userId, List<OrderItem> items) {
        if (items == null || items.isEmpty()) {
            throw new IllegalArgumentException("Order must have at least one item");
        }

        OrderNumber orderNumber = OrderNumber.generate();
        Money total = items.stream()
                .map(OrderItem::getTotalPrice)
                .reduce(new Money(java.math.BigDecimal.ZERO), Money::add);

        Instant now = Instant.now();
        return new Order(
                UUID.randomUUID(),
                orderNumber,
                userId,
                items,
                OrderStatus.PENDING,
                total,
                now,
                now
        );
    }

    public static Order restore(UUID id, OrderNumber orderNumber, UUID userId, List<OrderItem> items,
                                OrderStatus status, Money totalAmount,
                                Instant createdAt, Instant updatedAt) {
        return new Order(id,  orderNumber, userId, items, status, totalAmount, createdAt, updatedAt);
    }

    public void confirm() {
        if (status != OrderStatus.PENDING) {
            throw new InvalidOrderStatusException("Only pending orders can be confirmed");
        }

        this.status = OrderStatus.CONFIRMED;
        this.updatedAt = Instant.now();
    }

    public void ship() {
        if (status != OrderStatus.CONFIRMED) {
            throw new InvalidOrderStatusException("Only confirmed orders can be shipped");
        }

        this.status = OrderStatus.SHIPPED;
        this.updatedAt = Instant.now();
    }

    public void deliver() {
        if (status != OrderStatus.SHIPPED) {
            throw new InvalidOrderStatusException("Only shipped orders can be delivered");
        }

        this.status = OrderStatus.DELIVERED;
        this.updatedAt = Instant.now();
    }

    public void cancel() {
        if (status == OrderStatus.DELIVERED) {
            throw new OrderCancellationException("Cannot cancel a delivered order");
        }
        if (status == OrderStatus.CANCELED) {
            return;
        }
        this.status = OrderStatus.CANCELED;
        this.updatedAt = Instant.now();
    }

    public boolean isPending() {
        return status == OrderStatus.PENDING;
    }

    public boolean isCancelled() {
        return status == OrderStatus.CANCELED;
    }

    // getters

    public UUID getId() {
        return id;
    }

    public OrderNumber getOrderNumber() {
        return orderNumber;
    }

    public UUID getUserId() {
        return userId;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public Money getTotalAmount() {
        return totalAmount;
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
        Order order = (Order) o;
        return Objects.equals(id, order.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}

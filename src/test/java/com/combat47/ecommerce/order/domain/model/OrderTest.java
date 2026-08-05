package com.combat47.ecommerce.order.domain.model;

import com.combat47.ecommerce.order.domain.exception.OrderCancellationException;
import org.junit.jupiter.api.Test;
import org.mockito.internal.verification.Times;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class OrderTest {

    @Test
    void should_create_order() {
        UUID userId = UUID.randomUUID();
        OrderItem item = OrderItem.create(
                UUID.randomUUID(),
                "Product",
                new Money(new BigDecimal("99.99")),
                2
        );

        Order order = Order.create(userId, List.of(item));

        assertNotNull(order.getId());
        assertNotNull(order.getOrderNumber());
        assertEquals(userId, order.getUserId());
        assertEquals(OrderStatus.PENDING, order.getStatus());
        assertEquals(new BigDecimal("199.98"), order.getTotalAmount().getAmount());
        assertNotNull(order.getCreatedAt());
        assertNotNull(order.getUpdatedAt());
        assertEquals(order.getCreatedAt(), order.getUpdatedAt());
    }

    @Test
    void should_cancel_order() throws InterruptedException {
        Order order = Order.create(UUID.randomUUID(), List.of(
                OrderItem.create(UUID.randomUUID(), "Product",
                        new Money(new BigDecimal("10")), 1)
        ));
        Thread.sleep(100);
        order.cancel();
        assertEquals(OrderStatus.CANCELED, order.getStatus());
        System.out.println(order.getCreatedAt());
        System.out.println(order.getUpdatedAt());
        assertTrue(order.getUpdatedAt().isAfter(order.getCreatedAt()));
    }

    @Test
    void should_not_cancel_delivered_order() {
        Order order = Order.create(UUID.randomUUID(), List.of(
                OrderItem.create(UUID.randomUUID(), "Product",
                        new Money(new BigDecimal("10")), 1)
        ));
        order.confirm();
        order.ship();
        order.deliver();

        assertThrows(OrderCancellationException.class, order::cancel);
    }

}

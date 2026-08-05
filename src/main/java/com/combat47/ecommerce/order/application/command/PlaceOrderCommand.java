package com.combat47.ecommerce.order.application.command;

import com.combat47.ecommerce.order.domain.model.OrderItem;

import java.util.List;
import java.util.UUID;

public record PlaceOrderCommand(
        UUID userId,
        List<OrderItemCommand> items
) {
    public record OrderItemCommand(
            UUID productId,
            String productName,
            java.math.BigDecimal unitPrice,
            int quantity
    ) {}
}

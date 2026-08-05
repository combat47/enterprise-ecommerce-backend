package com.combat47.ecommerce.order.application.model;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record OrderResponse(
        UUID id,
        String orderNumber,
        UUID userId,
        List<OrderItemResponse> items,
        String status,
        BigDecimal totalAmount,
        Instant createdAt,
        Instant updatedAt
) {
    public record OrderItemResponse(
            UUID productId,
            String productName,
            BigDecimal unitPrice,
            int quantity,
            BigDecimal totalPrice
    ) {}
}

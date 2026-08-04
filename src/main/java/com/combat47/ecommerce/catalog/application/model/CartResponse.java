package com.combat47.ecommerce.catalog.application.model;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public record CartResponse(
        UUID cartId,
        UUID userId,
        List<CartItemResponse> items,
        BigDecimal total,
        int itemCount
) {
    public record CartItemResponse(
            UUID productId,
            String productName,
            BigDecimal price,
            int quantity,
            BigDecimal totalPrice
    ) {}
}

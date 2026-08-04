package com.combat47.ecommerce.catalog.application.command;

import java.util.UUID;

public record AddToCartCommand(
        UUID userId,
        UUID productId,
        String productName,
        java.math.BigDecimal price,
        int quantity
) {
}

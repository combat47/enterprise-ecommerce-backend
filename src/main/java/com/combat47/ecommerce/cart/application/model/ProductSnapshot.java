package com.combat47.ecommerce.cart.application.model;

import java.math.BigDecimal;
import java.util.UUID;

public record ProductSnapshot(
        UUID productId,
        String name,
        BigDecimal price,
        boolean active
) {
}

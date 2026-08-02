package com.combat47.ecommerce.catalog.application.model;

import java.math.BigDecimal;
import java.util.UUID;

public record ProductResponse(
        UUID id,
        String name,
        String description,
        BigDecimal price,
        String sku,
        boolean active
) {
}

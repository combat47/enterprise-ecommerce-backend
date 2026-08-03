package com.combat47.ecommerce.catalog.application.command;

import java.math.BigDecimal;
import java.util.UUID;

public record UpdateProductCommand(
        String name,
        String description,
        BigDecimal price,
        String sku
) {
}

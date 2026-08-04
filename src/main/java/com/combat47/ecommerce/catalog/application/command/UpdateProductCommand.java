package com.combat47.ecommerce.catalog.application.command;

import java.math.BigDecimal;

public record UpdateProductCommand(
        String name,
        String description,
        BigDecimal price,
        String sku
) {
}

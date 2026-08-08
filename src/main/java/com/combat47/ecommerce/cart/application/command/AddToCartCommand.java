package com.combat47.ecommerce.cart.application.command;

import java.math.BigDecimal;
import java.util.UUID;

public record AddToCartCommand(
        UUID customerId,
        UUID productId,
        int quantity
) {
}

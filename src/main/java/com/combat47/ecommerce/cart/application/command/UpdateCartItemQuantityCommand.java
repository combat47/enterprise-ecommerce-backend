package com.combat47.ecommerce.cart.application.command;

import java.util.UUID;

public record UpdateCartItemQuantityCommand(
        UUID customerId,
        UUID productId,
        int quantity
) {
}

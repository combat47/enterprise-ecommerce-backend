package com.combat47.ecommerce.cart.application.command;

import java.util.UUID;

public record RemoveFromCartCommand(
        UUID customerId,
        UUID productId
) {
}

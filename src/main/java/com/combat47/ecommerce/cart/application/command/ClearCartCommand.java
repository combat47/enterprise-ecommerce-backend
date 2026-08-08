package com.combat47.ecommerce.cart.application.command;

import java.util.UUID;

public record ClearCartCommand(
        UUID customerId
) {
}

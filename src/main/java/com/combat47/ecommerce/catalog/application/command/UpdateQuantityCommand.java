package com.combat47.ecommerce.catalog.application.command;

import java.util.UUID;

public record UpdateQuantityCommand(
        UUID userId,
        UUID productId,
        int quantity
) {
}

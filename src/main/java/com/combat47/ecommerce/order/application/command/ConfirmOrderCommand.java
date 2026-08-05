package com.combat47.ecommerce.order.application.command;

import java.util.UUID;

public record ConfirmOrderCommand(UUID orderId, UUID userId) {
}

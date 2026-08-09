package com.combat47.ecommerce.cart.infrastructure.web.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record UpdateCartItemQuantityRequest(
        @NotNull(message = "quantity cannot be null")
        @Min(value = 1, message = "quantity must be at least 1")
        Integer quantity
) {}

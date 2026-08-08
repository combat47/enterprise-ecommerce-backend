package com.combat47.ecommerce.cart.application.model;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public record CartResponse(
        UUID cartId,
        UUID customerId,
        List<CartItemResponse> items,
        BigDecimal totalPrice,
        int quantity
) {
}

package com.combat47.ecommerce.cart.application.model;

import java.math.BigDecimal;
import java.util.UUID;

public record CartItemResponse(
        UUID productId,
        String productName,
        BigDecimal unitPrice,
        int quantity,
        BigDecimal subtotal
) {
}

package com.combat47.ecommerce.cart.domain.exception;

public class CartNotFoundException extends RuntimeException {
    public CartNotFoundException(String message) {
        super(message);
    }

    public static CartNotFoundException forCustomerId(java.util.UUID customerId) {
        return new CartNotFoundException("Cart not found for customer: " + customerId);
    }
}

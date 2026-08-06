package com.combat47.ecommerce.cart.domain.exception;

public class CartIsEmptyException extends RuntimeException {
    public CartIsEmptyException(String message) {
        super(message);
    }
}

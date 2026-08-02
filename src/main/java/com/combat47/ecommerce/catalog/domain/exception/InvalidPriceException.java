package com.combat47.ecommerce.catalog.domain.exception;

public class InvalidPriceException extends RuntimeException {
    public InvalidPriceException(String message) {
        super(message);
    }
}

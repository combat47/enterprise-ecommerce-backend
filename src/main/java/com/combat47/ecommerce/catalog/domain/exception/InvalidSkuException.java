package com.combat47.ecommerce.catalog.domain.exception;

public class InvalidSkuException extends RuntimeException {
    public InvalidSkuException(String message) {
        super(message);
    }
}

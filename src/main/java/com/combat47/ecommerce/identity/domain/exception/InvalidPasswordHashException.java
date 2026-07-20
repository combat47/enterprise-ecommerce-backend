package com.combat47.ecommerce.identity.domain.exception;


public class InvalidPasswordHashException extends RuntimeException {
    public InvalidPasswordHashException(String message) {
        super(message);
    }

}

package com.combat47.ecommerce.identity.domain.exception;


public class InvalidEmailException extends RuntimeException {
    public InvalidEmailException(String message) {
        super(message);
    }

}

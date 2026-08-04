package com.combat47.ecommerce.catalog.domain.exception;

public class InvalidQuantityException extends RuntimeException {
  public InvalidQuantityException(String message) {
    super(message);
  }
}

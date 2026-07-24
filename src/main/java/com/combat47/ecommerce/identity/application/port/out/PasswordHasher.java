package com.combat47.ecommerce.identity.application.port.out;

public interface PasswordHasher {
    String hash(String rayPassword);
}

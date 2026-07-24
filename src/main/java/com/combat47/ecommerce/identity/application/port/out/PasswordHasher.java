package com.combat47.ecommerce.identity.application.port.out;


public interface PasswordHasher {

    String hash(String rayPassword);

    boolean matches(String rawPassword, String hashedPassword);
}

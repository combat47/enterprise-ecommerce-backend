package com.combat47.ecommerce.identity.infrastructure.security;

import com.combat47.ecommerce.identity.application.port.out.PasswordHasher;


public class FakePasswordHasher implements PasswordHasher {
    @Override
    public String hash(String rayPassword) {
        return "hashed-" + rayPassword;
    }

    @Override
    public boolean matches(String rawPassword, String hashedPassword) {
        return ("hashed-" + rawPassword).equals(hashedPassword);
    }
}

package com.combat47.ecommerce.identity.infrastructure.security;

import com.combat47.ecommerce.identity.application.port.out.PasswordHasher;
import com.combat47.ecommerce.identity.domain.model.PasswordHash;

public class FakePasswordHasher implements PasswordHasher {
    @Override
    public String hash(String rayPassword) {
        return "hashed-" + rayPassword;
    }
}

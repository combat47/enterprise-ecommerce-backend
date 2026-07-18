package com.combat47.ecommerce.identity.application.port.out;

import com.combat47.ecommerce.identity.domain.model.PasswordHash;

public interface PasswordHasher {
    PasswordHash hash(String rayPassword);
}

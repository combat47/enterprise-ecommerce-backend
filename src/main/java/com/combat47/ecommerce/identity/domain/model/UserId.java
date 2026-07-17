package com.combat47.ecommerce.identity.domain.model;

import java.util.UUID;

public record UserId(UUID value) {

    public static UserId generate() {
        return new UserId(UUID.randomUUID());
    }
}

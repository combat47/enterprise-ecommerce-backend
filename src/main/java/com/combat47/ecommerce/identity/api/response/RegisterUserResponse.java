package com.combat47.ecommerce.identity.api.response;

import java.time.Instant;
import java.util.UUID;

public record RegisterUserResponse(
        UUID userId,
        String message,
        Instant createdAt
) {
}

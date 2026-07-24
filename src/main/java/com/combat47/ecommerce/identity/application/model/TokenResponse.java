package com.combat47.ecommerce.identity.application.model;

public record TokenResponse(
        String accessToken,
        String refreshToken,
        long expiresIn
) {
}

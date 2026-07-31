package com.combat47.ecommerce.identity.domain.model;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

public class RefreshToken {

    private final UUID id;
    private final String token;
    private final UUID userId;
    private final Instant expiresAt;
    private boolean revoked;
    private final Instant createdAt;


    private RefreshToken(UUID id, String token, UUID userId, Instant expiresAt, boolean revoked, Instant createdAt) {
        this.id = id;
        this.token = token;
        this.userId = userId;
        this.expiresAt = expiresAt;
        this.revoked = revoked;
        this.createdAt = createdAt;
    }

    public static RefreshToken create(UUID userId, String token, Instant expiresAt) {

        Objects.requireNonNull(userId, "userId must not be null");
        Objects.requireNonNull(token, "token must not be null");
        Objects.requireNonNull(expiresAt, "expiresAt must not be null");

        if (token.isBlank()) {
            throw new IllegalArgumentException("Refresh token cannot be blank");
        }

        return new RefreshToken(
                UUID.randomUUID(),
                token,
                userId,
                expiresAt,
                false,
                Instant.now()
        );
    }

    public static RefreshToken restore(
            UUID id,
            String token,
            UUID userId,
            Instant expiresAt,
            boolean revoked,
            Instant createdAt) {
        Objects.requireNonNull(id, "id must not be null");
        Objects.requireNonNull(token, "token must not be null");
        Objects.requireNonNull(userId, "userId must not be null");
        Objects.requireNonNull(expiresAt, "expiresAt must not be null");
        Objects.requireNonNull(createdAt, "revoked must not be null");

        if (token.isBlank()) {
            throw new IllegalArgumentException("Refresh token cannot be blank");
        }

        return new RefreshToken(id, token, userId, expiresAt, revoked, createdAt);
    }

    public boolean isExpired() {
        return Instant.now().isAfter(expiresAt);
    }

    public boolean isValid() {
        return !isExpired() && !revoked;
    }

    public void revoke() {
        if (revoked) {
            return;
        }
        this.revoked = true;
    }


    public UUID getId() {
        return id;
    }

    public String getToken() {
        return token;
    }

    public UUID getUserId() {
        return userId;
    }

    public Instant getExpiresAt() {
        return expiresAt;
    }

    public boolean isRevoked() {
        return revoked;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }
}

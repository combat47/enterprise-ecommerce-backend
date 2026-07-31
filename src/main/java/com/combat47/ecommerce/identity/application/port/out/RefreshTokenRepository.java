package com.combat47.ecommerce.identity.application.port.out;

import com.combat47.ecommerce.identity.domain.model.RefreshToken;

import java.util.Optional;
import java.util.UUID;

public interface RefreshTokenRepository {

    RefreshToken save(RefreshToken refreshToken);

    Optional<RefreshToken> findByToken(String token);

    void revokeAllForUser(UUID userId);
}

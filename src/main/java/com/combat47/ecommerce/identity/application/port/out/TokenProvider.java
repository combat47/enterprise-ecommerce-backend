package com.combat47.ecommerce.identity.application.port.out;

import com.combat47.ecommerce.identity.application.model.TokenResponse;
import com.combat47.ecommerce.identity.domain.model.User;

import java.util.List;
import java.util.UUID;

public interface TokenProvider {

    TokenResponse generateToken(User user);

    boolean validateToken(String token);

    TokenResponse refreshToken(String refreshToken);

    UUID extractUserId(String token);

    List<String> extractRoles(String token);
}

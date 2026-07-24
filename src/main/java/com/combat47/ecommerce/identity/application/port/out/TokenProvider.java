package com.combat47.ecommerce.identity.application.port.out;

import com.combat47.ecommerce.identity.application.model.TokenResponse;
import com.combat47.ecommerce.identity.domain.model.User;

public interface TokenProvider {

    TokenResponse generateToken(User user);

    boolean validateToken(String token);

    TokenResponse refreshToken(String refreshToken);
}

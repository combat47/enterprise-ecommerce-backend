package com.combat47.ecommerce.identity.application.service;

import com.combat47.ecommerce.identity.application.command.RefreshTokenCommand;
import com.combat47.ecommerce.identity.application.model.TokenResponse;
import com.combat47.ecommerce.identity.application.port.in.RefreshTokenUseCase;
import com.combat47.ecommerce.identity.application.port.out.RefreshTokenRepository;
import com.combat47.ecommerce.identity.application.port.out.TokenProvider;
import com.combat47.ecommerce.identity.application.port.out.UserRepository;
import com.combat47.ecommerce.identity.domain.exception.InvalidRefreshTokenException;
import com.combat47.ecommerce.identity.domain.model.RefreshToken;
import com.combat47.ecommerce.identity.domain.model.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
public class RefreshTokenService implements RefreshTokenUseCase {

    private static final long REFRESH_TOKEN_EXPIRY_SECONDS = 30 * 24 * 60 * 60;

    private final RefreshTokenRepository refreshTokenRepository;
    private final TokenProvider tokenProvider;
    private final UserRepository userRepository;

    public RefreshTokenService(RefreshTokenRepository refreshTokenRepository,
                               TokenProvider tokenProvider,
                               UserRepository userRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.tokenProvider = tokenProvider;
        this.userRepository = userRepository;
    }


    @Override
    @Transactional
    public TokenResponse refreshToken(RefreshTokenCommand command) {
        String refreshTokenString = command.refreshToken();

        RefreshToken refreshToken = refreshTokenRepository.findByToken(refreshTokenString)
                .orElseThrow(() -> new InvalidRefreshTokenException("Invalid refresh token"));
        if (!refreshToken.isValid()) {
            throw new InvalidRefreshTokenException("Refresh token is expired or revoked");
        }

        User user = userRepository.findById(refreshToken.getUserId())
                .orElseThrow(() -> new InvalidRefreshTokenException("User not found"));

        refreshToken.revoke();
        refreshTokenRepository.save(refreshToken);

        TokenResponse tokenResponse = tokenProvider.generateToken(user);

        RefreshToken newRefreshToken = RefreshToken.create(
                user.getId(),
                tokenResponse.refreshToken(),
                Instant.now().plusSeconds(REFRESH_TOKEN_EXPIRY_SECONDS)
        );
        refreshTokenRepository.save(newRefreshToken);
        return tokenResponse;
    }
}
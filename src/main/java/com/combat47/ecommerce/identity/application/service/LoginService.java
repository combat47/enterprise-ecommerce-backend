package com.combat47.ecommerce.identity.application.service;


import com.combat47.ecommerce.identity.application.command.LoginCommand;
import com.combat47.ecommerce.identity.application.model.TokenResponse;
import com.combat47.ecommerce.identity.application.port.in.LoginUseCase;
import com.combat47.ecommerce.identity.application.port.out.PasswordHasher;
import com.combat47.ecommerce.identity.application.port.out.RefreshTokenRepository;
import com.combat47.ecommerce.identity.application.port.out.TokenProvider;
import com.combat47.ecommerce.identity.application.port.out.UserRepository;
import com.combat47.ecommerce.identity.domain.exception.InvalidCredentialsException;
import com.combat47.ecommerce.identity.domain.model.Email;
import com.combat47.ecommerce.identity.domain.model.RefreshToken;
import com.combat47.ecommerce.identity.domain.model.User;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;


@Component
public class LoginService implements LoginUseCase {

    private final static long REFRESH_TOKEN_EXPIRY_SECONDS = 30 * 24 * 60 * 60;

    private final UserRepository userRepository;

    private final PasswordHasher passwordHasher;

    private final TokenProvider tokenProvider;

    private final RefreshTokenRepository refreshTokenRepository;

    public LoginService(
            UserRepository userRepository,
            PasswordHasher passwordHasher, TokenProvider tokenProvider, RefreshTokenRepository refreshTokenRepository
    ) {
        this.userRepository = userRepository;
        this.passwordHasher = passwordHasher;
        this.tokenProvider = tokenProvider;
        this.refreshTokenRepository = refreshTokenRepository;
    }


    @Override
    @Transactional
    public TokenResponse login(LoginCommand command) {
        User user = userRepository.findByEmail(new Email(command.email()))
                .orElseThrow(() ->
                        new InvalidCredentialsException("Invalid email or password"));

        if (!passwordHasher.matches(command.password(), user.getPasswordHash().value())) {
            throw new InvalidCredentialsException("Invalid email or password");
        }

        refreshTokenRepository.revokeAllForUser(user.getId());

        TokenResponse tokenResponse = tokenProvider.generateToken(user);

        RefreshToken refreshToken = RefreshToken.create(
                user.getId(),
                tokenResponse.refreshToken(),
                Instant.now().plusSeconds(REFRESH_TOKEN_EXPIRY_SECONDS)
        );

        refreshTokenRepository.save(refreshToken);


        return tokenResponse;
    }
}

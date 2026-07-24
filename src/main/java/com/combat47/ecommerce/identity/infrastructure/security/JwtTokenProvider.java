package com.combat47.ecommerce.identity.infrastructure.security;

import com.combat47.ecommerce.identity.application.model.TokenResponse;
import com.combat47.ecommerce.identity.application.port.out.TokenProvider;
import com.combat47.ecommerce.identity.domain.model.*;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;
import java.util.Set;
import java.util.UUID;


@Component
public class JwtTokenProvider implements TokenProvider {

    private static final long ACCESS_TOKEN_EXPIRY = 15 * 60;

    private static final long REFRESH_TOKEN_EXPIRY = 30 * 24 * 60 * 60;

    private final SecretKey secretKey;

    public JwtTokenProvider(@Value("${jwt.secret}") String secret) {
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    @Override
    public TokenResponse generateToken(User user) {
        Instant now = Instant.now();

        String accessToken = Jwts.builder()
                .subject(user.getId().toString())
                .claim("email", user.getEmail().value())
                .claim("roles", user.getRoles().stream().map(Enum::name).toList())
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plusSeconds(ACCESS_TOKEN_EXPIRY)))
                .signWith(secretKey)
                .compact();

        String refreshToken = Jwts.builder()
                .subject(user.getId().toString())
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plusSeconds(REFRESH_TOKEN_EXPIRY)))
                .signWith(secretKey)
                .compact();

        return new TokenResponse(accessToken, refreshToken, ACCESS_TOKEN_EXPIRY);
    }

    @Override
    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public TokenResponse refreshToken(String refreshToken) {
        if (!validateToken(refreshToken)) {
            throw new IllegalArgumentException("Invalid refresh token");
        }

        Claims claims = Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(refreshToken)
                .getPayload();

        UUID userId = UUID.fromString(claims.getSubject());

        User user = User.restore(
                userId,
                new Email("temp@test.com"),
                new PasswordHash("temp"),
                new FirstName("temp"),
                new LastName("temp"),
                Set.of(),
                Instant.now(),
                Instant.now()
        );

        return generateToken(user);
    }
}

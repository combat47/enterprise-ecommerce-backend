package com.combat47.ecommerce.identity.infrastructure.security;

import com.combat47.ecommerce.identity.application.model.TokenResponse;
import com.combat47.ecommerce.identity.domain.model.*;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class JwtTokenProviderTest {

    private static final String SECRET_KEY = "my-very-secure-secret-key-that-is-at-least-32-characters-long";

    private JwtTokenProvider tokenProvider;

    private User testUser;

    private static final long EXPECTED_ACCESS_EXPIRY = 900;


    @BeforeEach
    void setup() {
        tokenProvider = new JwtTokenProvider(SECRET_KEY);
        testUser = User.register(
                new Email("amir@test.com"),
                new PasswordHash("hashed-password"),
                new FirstName("Amir"),
                new LastName("Jahazi")
        );
    }

    @Test
    void should_generate_valid_access_token() {

        TokenResponse tokenResponse = tokenProvider.generateToken(testUser);

        assertNotNull(tokenResponse);
        assertNotNull(tokenResponse.accessToken());
        assertNotNull(tokenResponse.refreshToken());
        assertEquals(EXPECTED_ACCESS_EXPIRY,  tokenResponse.expiresIn());

        var claims = extractClaims(tokenResponse.accessToken());
        assertEquals(testUser.getId().toString(), claims.getSubject());
        assertEquals(testUser.getEmail().value(), claims.get("email"));
    }

    @Test
    void should_validate_generated_token() {
        TokenResponse tokenResponse = tokenProvider.generateToken(testUser);

        boolean isValid = tokenProvider.validateToken(tokenResponse.accessToken());

        assertTrue(isValid);
    }

    @Test
    void should_reject_invalid_token() {
        String invalidToken = "invalid.token.here";

        boolean isValid = tokenProvider.validateToken(invalidToken);

        assertFalse(isValid);
    }

    @Test
    void should_reject_expired_token() {
        var secretKey = Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));

        String expiredToken = Jwts.builder()
                .subject(testUser.getId().toString())
                .claim("email", testUser.getEmail().value())
                .issuedAt(Date.from(Instant.now().minusSeconds(60)))
                .expiration(Date.from(Instant.now().minusSeconds(30)))
                .signWith(secretKey)
                .compact();

        boolean isValid = tokenProvider.validateToken(expiredToken);

        assertFalse(isValid);
    }

    @Test
    void should_contain_user_roles_in_claims() {
        User userWithRoles = User.register(
                new Email("amir@test.com"),
                new PasswordHash("hashed-password"),
                new FirstName("Amir"),
                new LastName("Jahazi")
        );

        userWithRoles.assignRole(Role.SELLER);
        userWithRoles.assignRole(Role.ADMIN);

        TokenResponse tokenResponse = tokenProvider.generateToken(userWithRoles);
        var claims = extractClaims(tokenResponse.accessToken());

        @SuppressWarnings("unchecked")
        List<String> roles = (List<String>) claims.get("roles");
        assertNotNull(roles);
        assertTrue(roles.contains(Role.CUSTOMER.toString()));
        assertTrue(roles.contains(Role.SELLER.toString()));
        assertTrue(roles.contains(Role.ADMIN.toString()));
        assertEquals(3, roles.size());

    }

    @Test
    void should_generate_valid_refresh_token() {
        TokenResponse tokenResponse = tokenProvider.generateToken(testUser);

        assertNotNull(tokenResponse.refreshToken());

        boolean isValid = tokenProvider.validateToken(tokenResponse.refreshToken());

        assertTrue(isValid);

        var claims = extractClaims(tokenResponse.refreshToken());
        assertEquals(testUser.getId().toString(), claims.getSubject());
    }


    private Claims extractClaims(String token) {
        var secretKey = Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));

        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}

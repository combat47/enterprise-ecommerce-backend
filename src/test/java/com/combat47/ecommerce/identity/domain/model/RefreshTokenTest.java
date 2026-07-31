package com.combat47.ecommerce.identity.domain.model;

import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class RefreshTokenTest {

    private static final UUID USER_ID = UUID.randomUUID();
    private static final String TOKEN = "test-refresh-token";
    private static final Instant EXPIRES_AT = Instant.now().plusSeconds(30 * 24 * 60 * 60);

    @Test
    void should_create_refresh_token() {
        RefreshToken refreshToken = RefreshToken.create(USER_ID, TOKEN, EXPIRES_AT);

        assertNotNull(refreshToken.getId());
        assertEquals(USER_ID, refreshToken.getUserId());
        assertEquals(TOKEN, refreshToken.getToken());
        assertEquals(EXPIRES_AT, refreshToken.getExpiresAt());
        assertFalse(refreshToken.isRevoked());
        assertNotNull(refreshToken.getCreatedAt());
        assertTrue(refreshToken.isValid());
        assertFalse(refreshToken.isExpired());
    }

    @Test
    void should_throw_exception_when_userId_is_null() {
        assertThrows(NullPointerException.class,
                () -> RefreshToken.create(null, TOKEN, EXPIRES_AT));
    }

    @Test
    void should_throw_exception_when_token_is_null() {
        assertThrows(NullPointerException.class,
                () -> RefreshToken.create(USER_ID, null, EXPIRES_AT) );
    }

    @Test
    void should_throw_exception_when_expiresAt_is_null() {
        assertThrows(NullPointerException.class,
                () ->  RefreshToken.create(USER_ID, TOKEN, null) );
    }

    @Test
    void should_throw_exception_when_token_is_blank() {
        assertThrows(IllegalArgumentException.class,
                () ->  RefreshToken.create(USER_ID, "   ", EXPIRES_AT) );
        assertThrows(IllegalArgumentException.class,
                () -> RefreshToken.create(USER_ID, "", EXPIRES_AT) );
    }

    @Test
    void should_not_refresh_when_expired() {
        Instant expired = Instant.now().minusSeconds(60);
        RefreshToken refreshToken = RefreshToken.create(USER_ID, TOKEN, expired);

        assertTrue(refreshToken.isExpired());
        assertFalse(refreshToken.isValid());
    }

    @Test
    void should_not_refresh_when_revoked() {
        RefreshToken refreshToken = RefreshToken.create(USER_ID, TOKEN, EXPIRES_AT);

        refreshToken.revoke();

        assertTrue(refreshToken.isRevoked());
        assertFalse(refreshToken.isValid());
        assertFalse(refreshToken.isExpired());
    }

    @Test
    void should_revoke_refresh_token() {
        RefreshToken refreshToken = RefreshToken.create(USER_ID, TOKEN, EXPIRES_AT);

        refreshToken.revoke();

        assertTrue(refreshToken.isRevoked());
        assertFalse(refreshToken.isValid());
    }

    @Test
    void should_allow_multiple_revoke_calls() {
        RefreshToken refreshToken = RefreshToken.create(USER_ID, TOKEN, EXPIRES_AT);

        refreshToken.revoke();
        refreshToken.revoke();
        refreshToken.revoke();

        assertTrue(refreshToken.isRevoked());
        assertFalse(refreshToken.isValid());
    }

    @Test
    void should_restore_refresh_token_from_database() {
        UUID id = UUID.randomUUID();
        Instant createdAt = Instant.now().minusSeconds(60);

        RefreshToken refreshToken = RefreshToken.restore(
                id,
                TOKEN,
                USER_ID,
                EXPIRES_AT,
                false,
                createdAt
        );

        assertEquals(id, refreshToken.getId());
        assertEquals(USER_ID, refreshToken.getUserId());
        assertEquals(TOKEN, refreshToken.getToken());
        assertEquals(EXPIRES_AT, refreshToken.getExpiresAt());
        assertFalse(refreshToken.isRevoked());
        assertEquals(createdAt, refreshToken.getCreatedAt());
        assertTrue(refreshToken.isValid());
        assertFalse(refreshToken.isExpired());

    }

    @Test
    void should_restore_revoked_token() {
        UUID id = UUID.randomUUID();
        Instant createdAt = Instant.now().minusSeconds(60);

        RefreshToken refreshToken = RefreshToken.restore(
                id,
                TOKEN,
                USER_ID,
                EXPIRES_AT,
                true,
                createdAt
        );

        assertTrue(refreshToken.isRevoked());
        assertFalse(refreshToken.isValid());
        assertFalse(refreshToken.isExpired());
    }

    @Test
    void should_restore_expired_token() {
        UUID id = UUID.randomUUID();
        Instant createdAt = Instant.now().minusSeconds(3600);
        Instant expiredAt = Instant.now().minusSeconds(60);

        RefreshToken refreshToken = RefreshToken.restore(
                id,
                TOKEN,
                USER_ID,
                expiredAt,
                false,
                createdAt
        );

        assertTrue(refreshToken.isExpired());
        assertFalse(refreshToken.isValid());
        assertFalse(refreshToken.isRevoked());
    }

    @Test
    void should_throw_exception_when_restore_with_null_id() {
        assertThrows(NullPointerException.class,
                () -> RefreshToken.restore(null, TOKEN,  USER_ID,  EXPIRES_AT,  false,
                        Instant.now()));
    }

    @Test
    void should_throw_exception_when_restore_with_null_token() {
        assertThrows(NullPointerException.class,
                () -> RefreshToken.restore(UUID.randomUUID(), null, USER_ID, EXPIRES_AT, false,
                        Instant.now()));
    }

    @Test
    void should_throw_exception_when_restore_with_blank_token() {
        assertThrows(IllegalArgumentException.class,
                () -> RefreshToken.restore(UUID.randomUUID(), "   ", USER_ID, EXPIRES_AT,
                        false, Instant.now()));
    }
}

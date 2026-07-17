package com.combat47.ecommerce.identity.domain.model;

import com.combat47.ecommerce.identity.domain.exception.InvalidPasswordHashException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PasswordHashTest {


    @Test
    void should_create_password_hash() {

        PasswordHash passwordHash = new PasswordHash("12345678");

        assertEquals(
                "12345678",
                passwordHash.value()
        );
    }


    @Test
    void should_reject_empty_password_hash() {

        assertThrows(
                InvalidPasswordHashException.class,
                () -> new PasswordHash("")
        );
    }


    @Test
    void should_reject_blank_password_hash() {

        assertThrows(
                InvalidPasswordHashException.class,
                () -> new PasswordHash("     ")
        );
    }


    @Test
    void should_reject_null_password_hash() {

        assertThrows(
                InvalidPasswordHashException.class,
                () -> new PasswordHash(null)
        );
    }

}

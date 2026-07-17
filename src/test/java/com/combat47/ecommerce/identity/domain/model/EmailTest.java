package com.combat47.ecommerce.identity.domain.model;

import com.combat47.ecommerce.identity.domain.exception.InvalidEmailException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EmailTest {

    @Test
    void should_create_valid_email() {
        Email email = new Email(" Amir@Example.COM ");

        assertEquals(
                "amir@example.com",
                email.value()
        );
    }


    @Test
    void should_reject_empty_email() {
        assertThrows(
                InvalidEmailException.class,
                () -> new Email("")
        );
    }


    @Test
    void should_reject_invalid_email() {
        assertThrows(
                InvalidEmailException.class,
                () -> new Email("invalid-email")
        );
    }
}
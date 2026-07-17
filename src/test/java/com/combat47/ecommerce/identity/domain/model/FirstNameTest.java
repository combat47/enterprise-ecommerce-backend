package com.combat47.ecommerce.identity.domain.model;

import com.combat47.ecommerce.identity.domain.exception.InvalidFirstNameException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FirstNameTest {


    @Test
    void should_create_valid_first_name() {

        assertEquals(
                "amir",
                new FirstName(" aMir").value()
        );
    }

    @Test
    void should_normalize_first_name() {

        assertAll(
                () -> assertEquals(
                        "john",
                        new FirstName("JOHN").value()
                ),
                () -> assertEquals(
                        "alice",
                        new FirstName("    alice    ").value()
                ),
                () -> assertEquals(
                        "mike",
                        new FirstName("   MikE    ").value()
                ),
                () -> assertEquals(
                        "ben",
                        new FirstName("ben").value()
                )
        );
    }

    @Test
    void should_reject_null_first_name() {

        assertThrows(
                InvalidFirstNameException.class,
                () -> new FirstName(null)
        );
    }

    @Test
    void should_reject_blank_first_name() {

        assertThrows(
                InvalidFirstNameException.class,
                () -> new FirstName("    ")
        );
    }


}

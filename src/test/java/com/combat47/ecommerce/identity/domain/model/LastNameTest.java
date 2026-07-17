package com.combat47.ecommerce.identity.domain.model;

import com.combat47.ecommerce.identity.domain.exception.InvalidLastNameException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LastNameTest {

    @Test
    void should_create_last_name() {

        assertEquals(
                "jahazi",
                new LastName("jahazi").value()
        );
    }


    @Test
    void should_normalize_last_name() {
        assertAll(
                () -> assertEquals(
                        "trump",
                        new LastName("TrUmp").value()
                ),
                () -> assertEquals(
                        "jacobs",
                        new LastName("   jacobs   ").value()
                ),
                () -> assertEquals(
                        "santos",
                        new LastName("   sAnTos    ").value()
                ),
                () -> assertEquals(
                        "jahazi",
                        new LastName("jahazi").value()
                )
        );
    }


    @Test
    void should_reject_null_last_name() {

        assertThrows(
                InvalidLastNameException.class,
                () -> new LastName(null)
        );
    }


    @Test
    void should_reject_blank_last_name() {
        assertThrows(
                InvalidLastNameException.class,
                () -> new LastName("    ")
        );
    }



}

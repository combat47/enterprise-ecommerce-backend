package com.combat47.ecommerce.identity.domain.model;

import com.combat47.ecommerce.identity.domain.exception.InvalidUserStateException;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    private User createUser() {
        Email email = new Email("amir@test.com");
        PasswordHash hash = new PasswordHash("hashedPassword");
        FirstName firstName = new FirstName("amir");
        LastName lastName = new LastName("jahazi");

        return User.register(email, hash, firstName, lastName);
    }


    @Test
    void should_create_user_with_pending_verification_status() {


        User user = createUser();

        assertNotNull(user.getId());
        assertEquals(UserStatus.PENDING_VERIFICATION, user.getUserStatus());
        assertNotNull(user.getCreatedAt());
        assertEquals(user.getCreatedAt(), user.getUpdatedAt());
        assertEquals("amir@test.com", user.getEmail().value());
        assertEquals("amir", user.getFirstName().value());
        assertEquals("jahazi", user.getLastName().value());
        assertEquals("hashedPassword", user.getPasswordHash().value());
    }

    @Test
    void should_active_user_when_pending() throws InterruptedException {

        User user = createUser();
        assertEquals(UserStatus.PENDING_VERIFICATION, user.getUserStatus());

        Instant oldUpdatedAt = user.getUpdatedAt();

        Thread.sleep(10);

        user.activate();

        assertEquals(UserStatus.ACTIVE, user.getUserStatus());

        assertTrue(user.getUpdatedAt().isAfter(oldUpdatedAt));
    }

    @Test
    void should_throw_exception_if_activate_already_active_user() {
        User user = createUser();
        user.activate();

        assertThrows(InvalidUserStateException.class, user::activate);
    }

    @Test
    void should_deactivate_active_user() {
        User user = createUser();
        user.activate();

        user.deactivate();

        assertEquals(UserStatus.INACTIVE, user.getUserStatus());
    }

    @Test
    void should_update_email_and_update_timestamp() throws InterruptedException {
        User user = createUser();
        Email newEmail = new Email("amirnewemail@test.com");
        Instant oldUpdatedAt = user.getUpdatedAt();

        Thread.sleep(10);

        user.changeEmail(newEmail);

        assertEquals(newEmail, user.getEmail());
        assertTrue(user.getUpdatedAt().isAfter(oldUpdatedAt));
    }
}

package com.combat47.ecommerce.identity.domain.model;

import com.combat47.ecommerce.identity.domain.exception.InvalidRoleOperationException;
import com.combat47.ecommerce.identity.domain.exception.InvalidUserStateException;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.Set;

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
    void should_create_user_with_default_customer_role() {
        User user = createUser();

        assertNotNull(user.getId());
        assertNotNull(user.getCreatedAt());
        assertEquals(user.getCreatedAt(), user.getUpdatedAt());

        assertEquals("amir@test.com", user.getEmail().value());
        assertEquals("hashedPassword", user.getPasswordHash().value());
        assertEquals("amir", user.getFirstName().value());
        assertEquals("jahazi", user.getLastName().value());

        Set<Role> roles = user.getRoles();
        assertEquals(1, roles.size());
        assertTrue(roles.contains(Role.CUSTOMER));
        assertFalse(roles.contains(Role.SELLER));
        assertFalse(roles.contains(Role.ADMIN));
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

    @Test
    void should_assign_new_role() {
        User user = createUser();
        user.assignRole(Role.SELLER);

        assertTrue(user.hasRole(Role.CUSTOMER));
        assertTrue(user.hasRole(Role.SELLER));
        assertEquals(2, user.getRoles().size());
    }

    @Test
    void should_remove_existing_role() throws InterruptedException {
        User user = createUser();

        user.assignRole(Role.SELLER);

        Instant oldUpdatedAt = Instant.now();
        Thread.sleep(10);

        user.removeRole(Role.SELLER);

        assertTrue(user.hasRole(Role.CUSTOMER));
        assertFalse(user.hasRole(Role.SELLER));
        assertEquals(1, user.getRoles().size());
        assertTrue(user.getUpdatedAt().isAfter(oldUpdatedAt));
    }

    @Test
    void should_throw_exception_when_removing_last_role() {
        User user = createUser();

        assertThrows(InvalidRoleOperationException.class, () -> user.removeRole(Role.CUSTOMER));

        assertTrue(user.hasRole(Role.CUSTOMER));
        assertEquals(1, user.getRoles().size());
    }

    @Test
    void hasRole_should_return_true_for_assigned_role() {
        User user = createUser();

        user.assignRole(Role.ADMIN);

        assertTrue(user.hasRole(Role.CUSTOMER));
        assertTrue(user.hasRole(Role.ADMIN));
    }

    @Test
    void hasRole_should_return_false_for_unassigned_role() {
        User user = createUser();

        assertFalse(user.hasRole(Role.SELLER));
        assertFalse(user.hasRole(Role.ADMIN));
    }

    @Test
    void assignedRole_should_update_timestamp() throws InterruptedException {
        User user = createUser();
        Instant oldUpdatedAt = Instant.now();
        Thread.sleep(10);

        user.assignRole(Role.SELLER);

        assertTrue(user.getUpdatedAt().isAfter(oldUpdatedAt));
    }

    @Test
    void removeRole_should_update_timestamp() throws InterruptedException {
        User user = createUser();
        user.assignRole(Role.SELLER);
        Instant oldUpdatedAt = Instant.now();
        Thread.sleep(10);

        user.removeRole(Role.SELLER);

        assertTrue(user.getUpdatedAt().isAfter(oldUpdatedAt));
    }


}






















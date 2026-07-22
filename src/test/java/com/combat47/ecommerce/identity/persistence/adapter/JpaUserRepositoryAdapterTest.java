package com.combat47.ecommerce.identity.persistence.adapter;

import com.combat47.ecommerce.identity.domain.model.*;
import com.combat47.ecommerce.identity.infrastructure.persistence.adapter.JpaUserRepositoryAdapter;
import com.combat47.ecommerce.identity.infrastructure.persistence.repository.JpaUserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class JpaUserRepositoryAdapterTest {

    @Autowired
    private JpaUserRepositoryAdapter adapter;

    @Autowired
    private JpaUserRepository jpaUserRepository;

    @Test
    void should_save_user_into_database() {
        String email = "adapter-" + UUID.randomUUID() + "@test.com";

        User user = User.register(
                new Email(email),
                new PasswordHash("hashed-password"),
                new FirstName("Amir"),
                new LastName("Jahazi")
        );

        User savedUser = adapter.save(user);

        assertNotNull(savedUser.getId());

        assertTrue(
                jpaUserRepository.existsByEmail(email)
        );
    }

    @Test
    void should_find_user_by_email() {

        String email = "find-" + UUID.randomUUID() + "@test.com";

        User user = User.register(
                new Email(email),
                new PasswordHash("hashed-password"),
                new FirstName("Amir"),
                new LastName("Jahazi")
        );

        adapter.save(user);

        Optional<User> found =
                adapter.findByEmail(new Email(email));

        assertTrue(found.isPresent());
        assertEquals(email, found.get().getEmail().value());
        assertEquals("amir", found.get().getFirstName().value());
        assertEquals("jahazi", found.get().getLastName().value());
    }
}

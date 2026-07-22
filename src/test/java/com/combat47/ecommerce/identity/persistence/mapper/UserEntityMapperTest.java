package com.combat47.ecommerce.identity.persistence.mapper;

import com.combat47.ecommerce.identity.domain.model.*;
import com.combat47.ecommerce.identity.infrastructure.persistence.entity.RoleEntity;
import com.combat47.ecommerce.identity.infrastructure.persistence.entity.UserEntity;
import com.combat47.ecommerce.identity.infrastructure.persistence.mapper.UserEntityMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class UserEntityMapperTest {

    private UserEntityMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new UserEntityMapper();
    }

    @Test
    void should_map_domain_user_to_entity() {
        User user = User.register(
                new Email("amir@test.com"),
                new PasswordHash("hashed-password"),
                new FirstName("Amir"),
                new LastName("Jahazi")
        );

        UserEntity entity = mapper.toEntity(user);

        assertEquals(user.getId(), entity.getId());
        assertEquals("amir@test.com", entity.getEmail());
        assertEquals("hashed-password", entity.getPasswordHash());
        assertEquals("amir", entity.getFirstName());
        assertEquals("jahazi", entity.getLastName());
        assertEquals(1, entity.getRoles().size());
        assertTrue(
                entity.getRoles()
                        .stream()
                        .anyMatch(roleEntity -> roleEntity.getRole().equals(Role.CUSTOMER.toString()))
        );
    }

    @Test
    void should_map_entity_to_domain_user() {
        UUID id = UUID.randomUUID();
        UserEntity entity = new UserEntity(
                id,
                "amir@test.com",
                "hashed-password",
                "amir",
                "jahazi",
                java.time.Instant.now(),
                java.time.Instant.now(),
                null
        );

        RoleEntity roleEntity = RoleEntity.create(entity, Role.CUSTOMER.toString());
        entity.setRoles(Set.of(roleEntity));

        User user = mapper.toDomain(entity);

        assertEquals(id, user.getId());
        assertEquals("amir@test.com", user.getEmail().value());
        assertEquals("hashed-password", user.getPasswordHash().value());
        assertEquals("amir", user.getFirstName().value());
        assertEquals("jahazi", user.getLastName().value());

        assertEquals(1, user.getRoles().size());
        assertTrue(user.getRoles().contains(Role.CUSTOMER));

    }
}

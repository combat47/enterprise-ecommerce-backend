package com.combat47.ecommerce.identity.infrastructure.persistence.inmemory;

import com.combat47.ecommerce.identity.application.port.out.UserRepository;
import com.combat47.ecommerce.identity.domain.model.Email;
import com.combat47.ecommerce.identity.domain.model.User;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class InMemoryUserRepository implements UserRepository {

    private final Map<UUID, User> users = new HashMap<>();

    @Override
    public boolean existsByEmail(Email email) {
        return users.values().stream()
                .anyMatch(user -> user.getEmail().equals(email));
    }

    @Override
    public User save(User user) {
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public Optional<User> findByEmail(Email email) {

        return users.values()
                .stream()
                .filter(user -> user.getEmail().equals(email))
                .findFirst();
    }
}

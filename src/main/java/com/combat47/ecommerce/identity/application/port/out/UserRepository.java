package com.combat47.ecommerce.identity.application.port.out;

import com.combat47.ecommerce.identity.domain.model.Email;
import com.combat47.ecommerce.identity.domain.model.User;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository {

    boolean existsByEmail(Email email);

    User save(User user);

    Optional<User> findByEmail(Email email);

    Optional<User> findById(UUID id);
}

package com.combat47.ecommerce.identity.integration;

import com.combat47.ecommerce.identity.application.command.RegisterUserCommand;
import com.combat47.ecommerce.identity.application.port.out.PasswordHasher;
import com.combat47.ecommerce.identity.application.port.out.UserRepository;
import com.combat47.ecommerce.identity.application.service.RegisterUserService;
import com.combat47.ecommerce.identity.domain.model.Email;
import com.combat47.ecommerce.identity.infrastructure.persistence.inmemory.InMemoryUserRepository;
import com.combat47.ecommerce.identity.infrastructure.security.FakePasswordHasher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RegisterUserIntegrationTest {

    private UserRepository repository;
    private PasswordHasher passwordHasher;
    private RegisterUserService service;


    @BeforeEach
    void setup() {
        repository = new InMemoryUserRepository();
        passwordHasher = new FakePasswordHasher();
        service = new RegisterUserService(repository, passwordHasher);
    }

    @Test
    void should_register_user_and_store_in_repository() {
        RegisterUserCommand command =
                new RegisterUserCommand("amir@test.com",
                        "12345678",
                        "Amir",
                        "Jahazi"
                        );

        service.execute(command);

        assertTrue(repository.existsByEmail(new Email("amir@test.com")));
    }


}

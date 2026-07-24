package com.combat47.ecommerce.identity.application.service;

import com.combat47.ecommerce.identity.application.command.RegisterUserCommand;
import com.combat47.ecommerce.identity.application.port.out.PasswordHasher;
import com.combat47.ecommerce.identity.application.port.out.UserRepository;
import com.combat47.ecommerce.identity.domain.exception.DuplicateEmailException;
import com.combat47.ecommerce.identity.domain.model.Email;
import com.combat47.ecommerce.identity.domain.model.PasswordHash;
import com.combat47.ecommerce.identity.domain.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RegisterUserServiceTest {

    private UserRepository userRepository;

    private PasswordHasher passwordHasher;

    private RegisterUserService registerUserService;


    private RegisterUserCommand command;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);

        passwordHasher = mock(PasswordHasher.class);

        registerUserService = new RegisterUserService(
                userRepository,
                passwordHasher
        );

        command = new RegisterUserCommand(
                "amir@test.com",
                "12345678",
                "Amir",
                "Jahazi"
        );
    }

    @Test
    void should_register_user_successfully_when_email_does_not_exist() {

        when(userRepository.existsByEmail(any(Email.class)))
                .thenReturn(false);

        when(passwordHasher.hash(anyString()))
                .thenReturn("hashed-password");

        registerUserService.execute(command);

        verify(userRepository, times(1)).save(any(User.class));

        verify(passwordHasher, times(1)).hash("12345678");
    }

    @Test
    void should_throw_exception_when_email_already_exists() {
        when(userRepository.existsByEmail(any(Email.class)))
                .thenReturn(true);

        assertThrows(DuplicateEmailException.class, () -> registerUserService.execute(command));

        verify(passwordHasher, never()).hash(anyString());

        verify(userRepository, never()).save(any(User.class));

        verify(userRepository, times(1)).existsByEmail(any(Email.class));
    }

}

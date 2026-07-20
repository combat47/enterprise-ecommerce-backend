package com.combat47.ecommerce.identity.application.service;

import com.combat47.ecommerce.identity.application.command.RegisterUserCommand;
import com.combat47.ecommerce.identity.application.port.in.RegisterUserUseCase;
import com.combat47.ecommerce.identity.application.port.out.PasswordHasher;
import com.combat47.ecommerce.identity.application.port.out.UserRepository;
import com.combat47.ecommerce.identity.domain.exception.DuplicateEmailException;
import com.combat47.ecommerce.identity.domain.model.*;

public class RegisterUserService implements RegisterUserUseCase {

    private final UserRepository userRepository;
    private final PasswordHasher passwordHasher;

    public RegisterUserService(UserRepository userRepository, PasswordHasher passwordHasher) {
        this.userRepository = userRepository;
        this.passwordHasher = passwordHasher;
    }

    @Override
    public User execute(RegisterUserCommand command) {
        Email email = new Email(command.email());
        if (userRepository.existsByEmail(email)) {
            throw new DuplicateEmailException("Email already exists: " + email.value());
        }
        FirstName firstName = new FirstName(command.firstname());
        LastName lastName = new LastName(command.lastname());

        PasswordHash passwordHash = passwordHasher.hash(command.password());

        User user = User.register(email, passwordHash, firstName, lastName);

        userRepository.save(user);
        return user;
    }
}

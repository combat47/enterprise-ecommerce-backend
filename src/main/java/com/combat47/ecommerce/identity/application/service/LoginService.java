package com.combat47.ecommerce.identity.application.service;


import com.combat47.ecommerce.identity.application.command.LoginCommand;
import com.combat47.ecommerce.identity.application.model.TokenResponse;
import com.combat47.ecommerce.identity.application.port.in.LoginUseCase;
import com.combat47.ecommerce.identity.application.port.out.PasswordHasher;
import com.combat47.ecommerce.identity.application.port.out.TokenProvider;
import com.combat47.ecommerce.identity.application.port.out.UserRepository;
import com.combat47.ecommerce.identity.domain.exception.InvalidCredentialsException;
import com.combat47.ecommerce.identity.domain.model.Email;
import com.combat47.ecommerce.identity.domain.model.User;
import org.springframework.stereotype.Component;



@Component
public class LoginService implements LoginUseCase {

    private final UserRepository userRepository;

    private final PasswordHasher passwordHasher;

    private final TokenProvider tokenProvider;

    public LoginService(
            UserRepository userRepository,
            PasswordHasher passwordHasher, TokenProvider tokenProvider
    ) {
        this.userRepository = userRepository;
        this.passwordHasher = passwordHasher;
        this.tokenProvider = tokenProvider;
    }


    @Override
    public TokenResponse login(LoginCommand command) {
        User user = userRepository.findByEmail(new Email(command.email()))
                .orElseThrow(() ->
                        new InvalidCredentialsException("Invalid email or password"));

        boolean matches = passwordHasher.matches(
                command.password(),
                user.getPasswordHash().value()
        );

        if (!matches) {
            throw new InvalidCredentialsException("Invalid email or password");
        }

        return tokenProvider.generateToken(user);
    }
}

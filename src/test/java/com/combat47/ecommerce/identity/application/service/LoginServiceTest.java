package com.combat47.ecommerce.identity.application.service;


import com.combat47.ecommerce.identity.application.command.LoginCommand;
import com.combat47.ecommerce.identity.application.model.TokenResponse;
import com.combat47.ecommerce.identity.application.port.out.PasswordHasher;
import com.combat47.ecommerce.identity.application.port.out.TokenProvider;
import com.combat47.ecommerce.identity.application.port.out.UserRepository;
import com.combat47.ecommerce.identity.domain.exception.InvalidCredentialsException;
import com.combat47.ecommerce.identity.domain.model.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LoginServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordHasher passwordHasher;

    @Mock
    private TokenProvider tokenProvider;

    @InjectMocks
    private LoginService loginService;


    @Test
    void should_login_when_credentials_are_valid() {

        String email = "amir@test.com";
        String rawPassword = "12345678";
        String hashedPassword = "hashed-12345678";

        User user = User.register(
                new Email(email),
                new PasswordHash(hashedPassword),
                new FirstName("Amir"),
                new LastName("Jahazi")
        );

        LoginCommand command = new LoginCommand(email, rawPassword);

        when(userRepository.findByEmail(any(Email.class)))
                .thenReturn(Optional.of(user));

        when(passwordHasher.matches(anyString(), anyString()))
                .thenReturn(true);

        TokenResponse tokenResponse = new TokenResponse(
                "access-token",
                "refresh-token",
                900
        );

        when(tokenProvider.generateToken(any(User.class)))
                .thenReturn(tokenResponse);

        TokenResponse result = loginService.login(command);

        assertNotNull(result, "Result should not be null");
        assertEquals("access-token", result.accessToken());
        assertEquals("refresh-token", result.refreshToken());
        assertEquals(900, result.expiresIn());

        verify(userRepository, times(1)).findByEmail(any(Email.class));
        verify(passwordHasher, times(1)).matches(anyString(), anyString());
        verify(tokenProvider, times(1)).generateToken(any(User.class));

    }


    @Test
    void should_throw_when_email_not_found() {

        String email = "notfound@test.com";
        String rawPassword = "password";
        LoginCommand command = new LoginCommand(email, rawPassword);

        when(userRepository.findByEmail(any(Email.class)))
                .thenReturn(Optional.empty());

        assertThrows(InvalidCredentialsException.class,
                () -> loginService.login(command));

        verify(passwordHasher, never()).matches(anyString(), anyString());
        verify(tokenProvider, never()).generateToken(any(User.class));
    }


    @Test
    void should_throw_when_password_is_invalid() {

        String email = "amir@test.com";
        String rawPassword = "12345678";
        String hashedPassword = "hashed-12345678";

        User user = User.register(
                new Email(email),
                new PasswordHash(hashedPassword),
                new FirstName("Amir"),
                new LastName("Jahazi")
        );

        LoginCommand command = new LoginCommand(email, rawPassword);

        when(userRepository.findByEmail(any(Email.class)))
                .thenReturn(Optional.of(user));

        when(passwordHasher.matches(anyString(), anyString()))
                .thenReturn(false);

        assertThrows(InvalidCredentialsException.class,
                () -> loginService.login(command));

        verify(passwordHasher, times(1)).matches(anyString(), anyString());
        verify(tokenProvider, never()).generateToken(any(User.class));

    }

}

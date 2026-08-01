package com.combat47.ecommerce.identity.infrastructure.security;

import com.combat47.ecommerce.identity.application.command.LoginCommand;
import com.combat47.ecommerce.identity.application.command.RefreshTokenCommand;
import com.combat47.ecommerce.identity.application.command.RegisterUserCommand;
import com.combat47.ecommerce.identity.application.model.TokenResponse;
import com.combat47.ecommerce.identity.application.port.in.LoginUseCase;
import com.combat47.ecommerce.identity.application.port.in.RefreshTokenUseCase;
import com.combat47.ecommerce.identity.application.port.in.RegisterUserUseCase;
import com.combat47.ecommerce.identity.domain.exception.DuplicateEmailException;
import com.combat47.ecommerce.identity.domain.exception.InvalidRefreshTokenException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@Transactional
class RefreshTokenIntegrationTest {

    @Autowired
    private RegisterUserUseCase registerUserUseCase;

    @Autowired
    private LoginUseCase loginUseCase;

    @Autowired
    private RefreshTokenUseCase refreshTokenUseCase;

    private String refreshToken;

    @BeforeEach
    void setUp() {
        registerUserIfNotExists("refresh@test.com", "12345678");
        TokenResponse loginResponse = loginUseCase.login(new LoginCommand("refresh@test.com", "12345678"));
        refreshToken = loginResponse.refreshToken();
    }

    private void registerUserIfNotExists(String email, String password) {
        try {
            registerUserUseCase.execute(new RegisterUserCommand(email, password, "Test", "User"));
        } catch (DuplicateEmailException ignored){
        }
    }

    @Test
    void should_refresh_successfully() throws InterruptedException {
        RefreshTokenCommand command = new RefreshTokenCommand(refreshToken);

        Thread.sleep(200);

        TokenResponse response = refreshTokenUseCase.refreshToken(command);

        assertNotNull(response);
        assertNotNull(response.accessToken());
        assertNotNull(response.refreshToken());
        assertNotEquals(refreshToken, response.refreshToken());
    }

    @Test
    void should_throw_for_invalid_refresh_token() {
        RefreshTokenCommand command = new RefreshTokenCommand("invalid-token");

        assertThrows(InvalidRefreshTokenException.class,
                () ->  refreshTokenUseCase.refreshToken(command));
    }

    @Test
    void should_throw_when_refresh_token_expired() {
        // این تست نیاز به دستکاری زمان دارد – در مرحله بعد کامل می‌شود
    }

}

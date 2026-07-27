package com.combat47.ecommerce.identity.infrastructure.security;

import com.combat47.ecommerce.identity.application.command.LoginCommand;
import com.combat47.ecommerce.identity.application.command.RegisterUserCommand;
import com.combat47.ecommerce.identity.application.port.in.LoginUseCase;
import com.combat47.ecommerce.identity.application.port.in.RegisterUserUseCase;
import com.combat47.ecommerce.identity.domain.exception.DuplicateEmailException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
class SecurityIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private RegisterUserUseCase registerUserUseCase;

    @Autowired
    private LoginUseCase loginUseCase;

    private String customerToken;

    @BeforeEach
    void setUp() {

        registerUserIfNotExists(
                "customer@test.com",
                "12345678"
        );

        customerToken = loginUseCase.login(
                new LoginCommand(
                        "customer@test.com",
                        "12345678"
                )
        ).accessToken();

        System.out.println("ACCESS TOKEN:");
        System.out.println(customerToken);
    }

    private void registerUserIfNotExists(String email, String password) {
        try {
            registerUserUseCase.execute(
                    new RegisterUserCommand(
                            email,
                            password,
                            "Test",
                            "User"
                    )
            );
        } catch (DuplicateEmailException ignored) {

        }
    }

    @Test
    void should_return_401_when_no_token() {

        ResponseEntity<String> response =
                restTemplate.exchange(
                        "/api/admin/test",
                        HttpMethod.GET,
                        HttpEntity.EMPTY,
                        String.class
                );

        assertEquals(
                HttpStatus.UNAUTHORIZED,
                response.getStatusCode()
        );
    }

    @Test
    @Disabled("Will be enabled after adding AdminController and @PreAuthorize")
    void should_authenticate_user_with_valid_token() {
//
//        HttpEntity<Void> request = new HttpEntity<>(createHeaders());
//
//
//        ResponseEntity<String> response =
//                restTemplate.exchange(
//                        "/api/admin/test",
//                        HttpMethod.GET,
//                        request,
//                        String.class
//                );
//
//        System.out.println("Response status: " + response.getStatusCode());
//
//        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    }

    private org.springframework.http.HttpHeaders createHeaders() {
        org.springframework.http.HttpHeaders headers =
                new org.springframework.http.HttpHeaders();

        headers.setBearerAuth(customerToken);

        return headers;
    }

}
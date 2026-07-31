package com.combat47.ecommerce.identity.infrastructure.security;

import com.combat47.ecommerce.identity.application.command.LoginCommand;
import com.combat47.ecommerce.identity.application.command.RegisterUserCommand;
import com.combat47.ecommerce.identity.application.port.in.LoginUseCase;
import com.combat47.ecommerce.identity.application.port.in.RegisterUserUseCase;
import com.combat47.ecommerce.identity.application.port.out.UserRepository;
import com.combat47.ecommerce.identity.domain.exception.DuplicateEmailException;
import com.combat47.ecommerce.identity.domain.model.Email;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
class SecurityAuthorizationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private RegisterUserUseCase registerUserUseCase;

    @Autowired
    private LoginUseCase loginUseCase;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private EntityManager entityManager;

    private String adminToken;
    private String customerToken;

    @BeforeEach
    void setUp() {

        registerUserIfNotExist("admin@test.com", "12345678");
        registerUserIfNotExist("customer@test.com", "12345678");

        UUID adminId = userRepository.findByEmail(new Email("admin@test.com"))
                .orElseThrow(() -> new RuntimeException("User not found"))
                .getId();

        jdbcTemplate.update(
                "INSERT INTO user_roles (id, user_id, role) VALUES (?, ?, ?) ON CONFLICT (user_id, role) DO NOTHING",
                UUID.randomUUID(), adminId, "ADMIN"
        );

        entityManager.flush();
        entityManager.clear();

        // دریافت توکن‌ها
        adminToken = loginUseCase.login(new LoginCommand("admin@test.com", "12345678")).accessToken();
        System.out.println("Admin Token: " + adminToken);
        customerToken = loginUseCase.login(new LoginCommand("customer@test.com", "12345678")).accessToken();
    }

    private void registerUserIfNotExist(String email, String password) {
        try {
            registerUserUseCase.execute(new RegisterUserCommand(email, password, "Test", "User"));
        } catch (DuplicateEmailException ignored) {
        }
    }

    @Test
    void should_return_401_when_no_token() {
        ResponseEntity<String> response = restTemplate.exchange(
                "/api/admin/test",
                HttpMethod.GET,
                HttpEntity.EMPTY,
                String.class
        );

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    void should_return_403_for_customer() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(customerToken);

        ResponseEntity<String> response = restTemplate.exchange(
                "/api/admin/test",
                HttpMethod.GET,
                new HttpEntity<>(headers),
                String.class
        );

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    }

    @Test
    void should_return_200_for_admin() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(adminToken);

        ResponseEntity<String> response = restTemplate.exchange(
                "/api/admin/test",
                HttpMethod.GET,
                new HttpEntity<>(headers),
                String.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("ADMIN_OK", response.getBody());
    }
}
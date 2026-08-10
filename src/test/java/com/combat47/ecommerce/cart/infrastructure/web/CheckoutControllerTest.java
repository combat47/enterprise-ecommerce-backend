package com.combat47.ecommerce.cart.infrastructure.web;

import com.combat47.ecommerce.cart.application.command.CheckoutCommand;
import com.combat47.ecommerce.cart.application.port.in.CheckoutUseCase;
import com.combat47.ecommerce.identity.domain.model.*;
import com.combat47.ecommerce.identity.infrastructure.security.CustomUserPrincipal;
import com.combat47.ecommerce.identity.infrastructure.security.JwtTokenProvider;
import com.combat47.ecommerce.order.application.model.OrderResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class CheckoutControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private CheckoutUseCase checkoutUseCase;

    @MockitoBean
    private JwtTokenProvider jwtTokenProvider; // برای بارگذاری context امنیتی

    private UUID customerId;
    private UsernamePasswordAuthenticationToken authToken;

    @BeforeEach
    void setUp() {
        customerId = UUID.randomUUID();

        Email email = new Email("test@test.com");
        PasswordHash passwordHash = new PasswordHash("hashed_password");
        FirstName firstName = new FirstName("Test");
        LastName lastName = new LastName("User");
        Set<Role> roles = Set.of(Role.CUSTOMER);
        Instant now = Instant.now();

        User user = User.restore(
                customerId,
                email,
                passwordHash,
                firstName,
                lastName,
                roles,
                now,
                now
        );

        CustomUserPrincipal principal = new CustomUserPrincipal(user);
        authToken = new UsernamePasswordAuthenticationToken(
                principal,
                null,
                principal.getAuthorities()
        );
    }

    @Test
    void shouldCheckoutSuccessfully() throws Exception {
        OrderResponse response = new OrderResponse(
                UUID.randomUUID(),
                "ORD-12345678",
                customerId,
                List.of(),
                "PENDING",
                BigDecimal.TEN,
                Instant.now(),
                Instant.now()
        );

        when(checkoutUseCase.checkout(any(CheckoutCommand.class))).thenReturn(response);

        mockMvc.perform(post("/api/v1/cart/checkout")
                        .with(authentication(authToken))
                        .with(csrf()))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.userId").value(customerId.toString()))
                .andExpect(jsonPath("$.status").value("PENDING"));

        verify(checkoutUseCase, times(1)).checkout(any(CheckoutCommand.class));
    }

    // سایر تست‌ها به‌همین صورت با @SpringBootTest کار می‌کنند
}
package com.combat47.ecommerce.cart.infrastructure.web;

import com.combat47.ecommerce.cart.application.command.*;
import com.combat47.ecommerce.cart.application.model.CartResponse;
import com.combat47.ecommerce.cart.application.port.in.*;
import com.combat47.ecommerce.cart.domain.exception.CartNotFoundException;
import com.combat47.ecommerce.cart.infrastructure.web.request.AddToCartRequest;
import com.combat47.ecommerce.cart.infrastructure.web.request.UpdateCartItemQuantityRequest;
import com.combat47.ecommerce.identity.domain.model.Email;
import com.combat47.ecommerce.identity.domain.model.FirstName;
import com.combat47.ecommerce.identity.domain.model.LastName;
import com.combat47.ecommerce.identity.domain.model.PasswordHash;
import com.combat47.ecommerce.identity.domain.model.User;
import com.combat47.ecommerce.identity.infrastructure.security.CustomUserPrincipal;
import com.combat47.ecommerce.identity.infrastructure.security.JwtTokenProvider;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class CartControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private AddToCartUseCase addToCartUseCase;

    @MockitoBean
    private GetCartUseCase getCartUseCase;

    @MockitoBean
    private UpdateCartItemQuantityUseCase updateCartItemQuantityUseCase;

    @MockitoBean
    private RemoveFromCartUseCase removeFromCartUseCase;

    @MockitoBean
    private ClearCartUseCase clearCartUseCase;

    @MockitoBean
    private JwtTokenProvider jwtTokenProvider;

    private UUID customerId;
    private UUID productId;
    private UsernamePasswordAuthenticationToken authToken;

    @BeforeEach
    void setUp() {

        productId = UUID.randomUUID();

        User user = User.register(
                new Email("test@test.com"),
                new PasswordHash("hashed-password"),
                new FirstName("Amir"),
                new LastName("Jahazi")
        );

        /*
         * مهم:
         * customerId باید همان ID کاربری باشد که در Authentication
         * قرار گرفته است.
         */
        customerId = user.getId();

        CustomUserPrincipal principal = new CustomUserPrincipal(user);

        authToken = new UsernamePasswordAuthenticationToken(
                principal,
                null,
                principal.getAuthorities()
        );
    }

    // ============================================================
    // ADD TO CART
    // ============================================================

    @Test
    void addToCart_withValidRequest_shouldReturn201() throws Exception {

        AddToCartRequest request =
                new AddToCartRequest(productId, 2);

        CartResponse response = new CartResponse(
                UUID.randomUUID(),
                customerId,
                List.of(),
                BigDecimal.TEN,
                2
        );

        when(addToCartUseCase.addToCart(any(AddToCartCommand.class)))
                .thenReturn(response);

        mockMvc.perform(
                        post("/api/v1/cart/items")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                                .with(authentication(authToken))
                                .with(csrf())
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.customerId")
                        .value(customerId.toString()));

        verify(addToCartUseCase)
                .addToCart(any(AddToCartCommand.class));
    }

    @Test
    void addToCart_withZeroQuantity_shouldReturn400() throws Exception {

        AddToCartRequest request =
                new AddToCartRequest(productId, 0);

        mockMvc.perform(
                        post("/api/v1/cart/items")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                                .with(authentication(authToken))
                                .with(csrf())
                )
                .andExpect(status().isBadRequest());

        verifyNoInteractions(addToCartUseCase);
    }

    @Test
    void addToCart_withNullProductId_shouldReturn400() throws Exception {

        AddToCartRequest request =
                new AddToCartRequest(null, 2);

        mockMvc.perform(
                        post("/api/v1/cart/items")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                                .with(authentication(authToken))
                                .with(csrf())
                )
                .andExpect(status().isBadRequest());

        verifyNoInteractions(addToCartUseCase);
    }

    @Test
    void addToCart_withoutAuthentication_shouldReturn401() throws Exception {

        AddToCartRequest request =
                new AddToCartRequest(productId, 2);

        mockMvc.perform(
                        post("/api/v1/cart/items")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                                .with(csrf())
                )
                .andExpect(status().isUnauthorized());

        verifyNoInteractions(addToCartUseCase);
    }

    // ============================================================
    // GET CART
    // ============================================================

    @Test
    void getCart_shouldReturnCart() throws Exception {

        CartResponse response = new CartResponse(
                UUID.randomUUID(),
                customerId,
                List.of(),
                BigDecimal.ZERO,
                0
        );

        when(getCartUseCase.getCart(any(GetCartCommand.class)))
                .thenReturn(response);

        mockMvc.perform(
                        get("/api/v1/cart")
                                .with(authentication(authToken))
                                .with(csrf())
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.customerId")
                        .value(customerId.toString()));

        verify(getCartUseCase)
                .getCart(any(GetCartCommand.class));
    }

    @Test
    void getCart_whenCartNotFound_shouldReturn404() throws Exception {

        when(getCartUseCase.getCart(any(GetCartCommand.class)))
                .thenThrow(
                        new CartNotFoundException("Cart not found")
                );

        mockMvc.perform(
                        get("/api/v1/cart")
                                .with(authentication(authToken))
                                .with(csrf())
                )
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error")
                        .value("Cart not found"));
    }

    // ============================================================
    // UPDATE QUANTITY
    // ============================================================

    @Test
    void updateQuantity_withValidRequest_shouldReturn200() throws Exception {

        UpdateCartItemQuantityRequest request =
                new UpdateCartItemQuantityRequest(5);

        CartResponse response = new CartResponse(
                UUID.randomUUID(),
                customerId,
                List.of(),
                BigDecimal.TEN,
                5
        );

        when(updateCartItemQuantityUseCase
                .updateCartItemQuantity(any(UpdateCartItemQuantityCommand.class)))
                .thenReturn(response);

        mockMvc.perform(
                        put("/api/v1/cart/items/{productId}", productId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                                .with(authentication(authToken))
                                .with(csrf())
                )
                .andExpect(status().isOk());

        verify(updateCartItemQuantityUseCase)
                .updateCartItemQuantity(any(UpdateCartItemQuantityCommand.class));
    }

    @Test
    void updateQuantity_withZeroQuantity_shouldReturn400() throws Exception {

        UpdateCartItemQuantityRequest request =
                new UpdateCartItemQuantityRequest(0);

        mockMvc.perform(
                        put("/api/v1/cart/items/{productId}", productId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                                .with(authentication(authToken))
                                .with(csrf())
                )
                .andExpect(status().isBadRequest());

        verifyNoInteractions(updateCartItemQuantityUseCase);
    }

    @Test
    void removeItem_shouldReturn204() throws Exception {

        CartResponse response = new CartResponse(
                UUID.randomUUID(),
                customerId,
                List.of(),
                BigDecimal.ZERO,
                0
        );

        when(removeFromCartUseCase
                .removeFromCart(any(RemoveFromCartCommand.class)))
                .thenReturn(response);

        mockMvc.perform(
                        delete("/api/v1/cart/items/{productId}", productId)
                                .with(authentication(authToken))
                                .with(csrf())
                )
                .andExpect(status().isNoContent());

        verify(removeFromCartUseCase)
                .removeFromCart(any(RemoveFromCartCommand.class));
    }

    @Test
    void clearCart_shouldReturn204() throws Exception {

        CartResponse response = new CartResponse(
                UUID.randomUUID(),
                customerId,
                List.of(),
                BigDecimal.ZERO,
                0
        );

        when(clearCartUseCase
                .clearCart(any(ClearCartCommand.class)))
                .thenReturn(response);

        mockMvc.perform(
                        delete("/api/v1/cart")
                                .with(authentication(authToken))
                                .with(csrf())
                )
                .andExpect(status().isNoContent());

        verify(clearCartUseCase)
                .clearCart(any(ClearCartCommand.class));
    }


    @Test
    void getCart_shouldUseCustomerIdFromAuthentication() throws Exception {

        when(getCartUseCase.getCart(any(GetCartCommand.class)))
                .thenReturn(
                        new CartResponse(
                                UUID.randomUUID(),
                                customerId,
                                List.of(),
                                BigDecimal.ZERO,
                                0
                        )
                );

        mockMvc.perform(
                        get("/api/v1/cart")
                                .with(authentication(authToken))
                                .with(csrf())
                )
                .andExpect(status().isOk());

        verify(getCartUseCase)
                .getCart(argThat(command ->
                        command.customerId().equals(customerId)
                ));
    }
}
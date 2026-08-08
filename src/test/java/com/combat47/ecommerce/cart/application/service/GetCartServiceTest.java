package com.combat47.ecommerce.cart.application.service;

import com.combat47.ecommerce.cart.application.command.GetCartCommand;
import com.combat47.ecommerce.cart.application.mapper.CartResponseMapper;
import com.combat47.ecommerce.cart.application.model.CartResponse;
import com.combat47.ecommerce.cart.application.port.out.CartRepository;
import com.combat47.ecommerce.cart.domain.exception.CartNotFoundException;
import com.combat47.ecommerce.cart.domain.model.Cart;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GetCartServiceTest {

    @Mock
    private CartRepository cartRepository;

    @Mock
    private CartResponseMapper mapper;

    @InjectMocks
    private CartGetProductService service;

    private static final UUID CUSTOMER_ID = UUID.randomUUID();

    private final GetCartCommand command = new GetCartCommand(CUSTOMER_ID);

    @Test
    void should_throw_when_cart_not_found() {

        when(cartRepository.findByCustomerId(CUSTOMER_ID)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.getCart(command))
                .isInstanceOf(CartNotFoundException.class)
                .hasMessage("Cart not found for customer: " + CUSTOMER_ID);

        verify(cartRepository).findByCustomerId(CUSTOMER_ID);
        verify(cartRepository, never()).save(any());
        verify(mapper, never()).toResponse(any());
    }

    @Test
    void should_get_cart_and_return_response_without_saving() {

        Cart cart = Cart.create(CUSTOMER_ID);
        when(cartRepository.findByCustomerId(CUSTOMER_ID)).thenReturn(Optional.of(cart));

        CartResponse expectedResponse = new CartResponse(
                cart.getId(),
                CUSTOMER_ID,
                List.of(),
                BigDecimal.ZERO,
                0
        );
        when(mapper.toResponse(any(Cart.class))).thenReturn(expectedResponse);

        CartResponse response = service.getCart(command);

        assertThat(response).isNotNull();
        assertThat(response.customerId()).isEqualTo(CUSTOMER_ID);
        assertThat(response.quantity()).isZero();

        verify(cartRepository).findByCustomerId(CUSTOMER_ID);
        verify(cartRepository, never()).save(any());
        verify(mapper).toResponse(cart);
    }
}
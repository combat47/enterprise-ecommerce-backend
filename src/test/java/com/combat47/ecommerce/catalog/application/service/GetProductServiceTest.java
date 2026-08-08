package com.combat47.ecommerce.catalog.application.service;

import com.combat47.ecommerce.catalog.application.command.GetCartCommand;
import com.combat47.ecommerce.catalog.application.port.out.CartRepository;
import com.combat47.ecommerce.catalog.domain.exception.CartItemNotFoundException;
import com.combat47.ecommerce.catalog.domain.model.Cart;
import com.combat47.ecommerce.catalog.domain.model.CartItem;
import com.combat47.ecommerce.catalog.domain.model.Price;
import com.combat47.ecommerce.catalog.domain.model.Quantity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GetProductServiceTest {

    @Mock
    private CartRepository cartRepository;

    @InjectMocks
    private CatalogGetProductService getCartService;

    private static final UUID USER_ID = UUID.randomUUID();

    @Test
    void should_get_cart() {
        Cart cart = Cart.create(USER_ID);
        CartItem item = CartItem.create(UUID.randomUUID(), "Product", new Price(new BigDecimal("10.00")), new Quantity(2));
        cart.addItem(item);

        when(cartRepository.findByUserId(USER_ID)).thenReturn(Optional.of(cart));

        var response = getCartService.getCart(new GetCartCommand(USER_ID));

        assertNotNull(response);
        assertEquals(1, response.itemCount());
        assertEquals(new BigDecimal("20.00"), response.total());
        verify(cartRepository, times(1)).findByUserId(USER_ID);
    }

    @Test
    void should_throw_if_cart_not_found() {
        when(cartRepository.findByUserId(USER_ID)).thenReturn(Optional.empty());

        assertThrows(CartItemNotFoundException.class,
                () -> getCartService.getCart(new GetCartCommand(USER_ID)));
    }
}
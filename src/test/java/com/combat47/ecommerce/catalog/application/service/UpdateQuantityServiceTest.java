package com.combat47.ecommerce.catalog.application.service;

import com.combat47.ecommerce.catalog.application.command.UpdateQuantityCommand;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UpdateQuantityServiceTest {

    @Mock
    private CartRepository cartRepository;

    @InjectMocks
    private UpdateQuantityService updateQuantityService;

    private static final UUID USER_ID = UUID.randomUUID();
    private static final UUID PRODUCT_ID = UUID.randomUUID();

    @Test
    void should_update_quantity() {
        Cart cart = Cart.create(USER_ID);
        CartItem item = CartItem.create(PRODUCT_ID, "Product", new Price(new BigDecimal("10.00")), new Quantity(1));
        cart.addItem(item);

        when(cartRepository.findByUserId(USER_ID)).thenReturn(Optional.of(cart));
        when(cartRepository.save(any(Cart.class))).thenAnswer(i -> i.getArgument(0));

        var response = updateQuantityService.updateQuantity(new UpdateQuantityCommand(USER_ID, PRODUCT_ID, 5));

        assertEquals(5, response.items().getFirst().quantity());
        verify(cartRepository, times(1)).save(any(Cart.class));
    }

    @Test
    void should_throw_if_cart_not_found() {
        when(cartRepository.findByUserId(USER_ID)).thenReturn(Optional.empty());

        assertThrows(CartItemNotFoundException.class,
                () -> updateQuantityService.updateQuantity(new UpdateQuantityCommand(USER_ID, PRODUCT_ID, 5)));

        verify(cartRepository, never()).save(any(Cart.class));
    }

    @Test
    void should_throw_if_product_not_found() {
        Cart cart = Cart.create(USER_ID);
        when(cartRepository.findByUserId(USER_ID)).thenReturn(Optional.of(cart));

        assertThrows(CartItemNotFoundException.class,
                () -> updateQuantityService.updateQuantity(new UpdateQuantityCommand(USER_ID, PRODUCT_ID, 5)));

        verify(cartRepository, never()).save(any(Cart.class));
    }
}
package com.combat47.ecommerce.cart.infrastructure.config;

import com.combat47.ecommerce.cart.application.mapper.CartResponseMapper;
import com.combat47.ecommerce.cart.application.port.in.*;
import com.combat47.ecommerce.cart.application.port.out.CartRepository;
import com.combat47.ecommerce.cart.application.port.out.ProductCatalogPort;
import com.combat47.ecommerce.cart.application.service.*;
import com.combat47.ecommerce.cart.infrastructure.persistence.inmemory.InMemoryCartRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CartConfiguration {

    @Bean
    public CartRepository cartRepository() {
        return new InMemoryCartRepository();
    }

    @Bean
    public CartResponseMapper cartResponseMapper() {
        return new CartResponseMapper();
    }

    @Bean
    public AddToCartUseCase addToCartUseCase(CartRepository cartRepository,
                                             ProductCatalogPort productCatalogPort,
                                             CartResponseMapper mapper) {
        return new CartAddToCartService(cartRepository, productCatalogPort, mapper);
    }

    @Bean
    public RemoveFromCartUseCase removeFromCartUseCase(CartRepository cartRepository,
                                                       CartResponseMapper mapper) {
        return new CartRemoveFromCartService(cartRepository, mapper);
    }

    @Bean
    public UpdateCartItemQuantityUseCase updateCartItemQuantityUseCase(CartRepository cartRepository,
                                                                       CartResponseMapper mapper) {
        return new UpdateCartItemQuantityService(cartRepository, mapper);
    }

    @Bean
    public ClearCartUseCase clearCartUseCase(CartRepository cartRepository,
                                             CartResponseMapper mapper) {
        return new ClearCartService(cartRepository, mapper);
    }

    @Bean
    public GetCartUseCase getCartUseCase(CartRepository cartRepository,
                                         CartResponseMapper mapper) {
        return new CartGetProductService(cartRepository, mapper);
    }
}

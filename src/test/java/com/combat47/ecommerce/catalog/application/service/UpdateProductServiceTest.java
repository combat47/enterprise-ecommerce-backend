package com.combat47.ecommerce.catalog.application.service;

import com.combat47.ecommerce.catalog.application.command.UpdateProductCommand;
import com.combat47.ecommerce.catalog.application.model.ProductResponse;
import com.combat47.ecommerce.catalog.application.port.out.ProductRepository;
import com.combat47.ecommerce.catalog.domain.exception.ProductNotFoundException;
import com.combat47.ecommerce.catalog.domain.model.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class UpdateProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private UpdateProductService updateProductService;

    private final Product existingProduct = Product.create(
            new ProductName("Old Name"),
            new Description("Old Description"),
            new Price(new BigDecimal("99.99")),
            new Sku("OLD-001")
    );

    private final UpdateProductCommand command = new UpdateProductCommand(
            "New Name",
            "New Description",
            new BigDecimal("99.99"),
            "OLD-001"
    );

    @Test
    void should_update_product_successfully() {
        when(productRepository.findBySku(any(Sku.class))).thenReturn(Optional.of(existingProduct));
        when(productRepository.save(any(Product.class))).thenAnswer(invocation ->
                invocation.getArgument(0));

        ProductResponse response = updateProductService.update(command);

        assertNotNull(response);
        assertEquals("New Name", response.name());
        assertEquals("New Description", response.description());
        assertEquals(new BigDecimal("99.99"), response.price());
        assertEquals("OLD-001", response.sku());
        assertTrue(response.active());

        verify(productRepository, times(1)).findBySku(any(Sku.class));
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    void should_throw_when_product_not_found() {
        when(productRepository.findBySku(any(Sku.class))).thenReturn(Optional.empty());

        assertThrows(ProductNotFoundException.class,
                () ->  updateProductService.update(command));

        verify(productRepository,never()).save(any(Product.class));
    }

}

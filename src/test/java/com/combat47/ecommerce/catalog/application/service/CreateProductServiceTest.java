package com.combat47.ecommerce.catalog.application.service;

import com.combat47.ecommerce.catalog.application.command.CreateProductCommand;
import com.combat47.ecommerce.catalog.application.model.ProductResponse;
import com.combat47.ecommerce.catalog.application.port.out.ProductRepository;
import com.combat47.ecommerce.catalog.domain.exception.DuplicateSkuException;
import com.combat47.ecommerce.catalog.domain.model.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class CreateProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private CreateProductService createProductService;

    private final CreateProductCommand validCommand = new CreateProductCommand(
            "Laptop",
            "Powerful laptop",
            new BigDecimal("999.99"),
            "LAP-001"
    );

    @Test
    void should_create_product_successfully() {
        when(productRepository.existsBySku(any(Sku.class))).thenReturn(false);

        Product product = Product.create(
                new ProductName("Laptop"),
                new Description("Powerful laptop"),
                new Price(new BigDecimal("999.99")),
                new Sku("LAP-001")
        );

        when(productRepository.save(any(Product.class))).thenReturn(product);

        ProductResponse response =  createProductService.create(validCommand);

        assertNotNull(response);
        assertEquals(product.getId(), response.id());
        assertEquals("Laptop", response.name());
        assertEquals("Powerful laptop", response.description());
        assertEquals(new BigDecimal("999.99"), response.price());
        assertEquals("LAP-001", response.sku());
        assertTrue(response.active());

        verify(productRepository, times(1)).existsBySku(any(Sku.class));
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    void should_throw_when_sku_already_exists() {
        when(productRepository.existsBySku(any(Sku.class))).thenReturn(true);

        assertThrows(DuplicateSkuException.class,
                () ->  createProductService.create(validCommand));

        verify(productRepository, times(1)).existsBySku(any(Sku.class));
        verify(productRepository, never()).save(any(Product.class));
    }

}
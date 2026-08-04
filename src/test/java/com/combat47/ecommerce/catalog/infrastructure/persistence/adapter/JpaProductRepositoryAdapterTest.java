package com.combat47.ecommerce.catalog.infrastructure.persistence.adapter;


import com.combat47.ecommerce.catalog.application.port.out.ProductRepository;
import com.combat47.ecommerce.catalog.domain.model.*;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@Transactional
class JpaProductRepositoryAdapterTest {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private EntityManager entityManager;

    private Product testProduct;

    @BeforeEach
    void setUp() {
        testProduct = Product.create(
                new ProductName("Test Product"),
                new Description("Test Description"),
                new Price(new BigDecimal("99.99")),
                new Sku("TEST-001")
        );
    }

    @Test
    void should_save_and_find_by_id() {
        Product saved = productRepository.save(testProduct);
        entityManager.flush();

        assertNotNull(saved.getId());

        Product found = productRepository.findById(saved.getId()).orElseThrow();
        assertEquals(saved.getId(), found.getId());
        assertEquals("Test Product", found.getName().getValue());
    }

    @Test
    void should_find_by_sku() {
        Product saved = productRepository.save(testProduct);
        entityManager.flush();

        Product found = productRepository.findBySku(new Sku("TEST-001")).orElseThrow();
        assertEquals(saved.getId(), found.getId());
    }

    @Test
    void should_return_only_active_products() {
        Product active1 = Product.create(
                new ProductName("Active 1"),
                new Description("Desc 1"),
                new Price(new BigDecimal("10.00")),
                new Sku("ACT-001")
        );
        Product active2 = Product.create(
                new ProductName("Active 2"),
                new Description("Desc 2"),
                new Price(new BigDecimal("20.00")),
                new Sku("ACT-002")
        );
        Product inactive = Product.create(
                new ProductName("Inactive"),
                new Description("Desc 3"),
                new Price(new BigDecimal("30.00")),
                new Sku("INACT-001")
        );

        productRepository.save(active1);
        productRepository.save(active2);
        productRepository.save(inactive);
        entityManager.flush();

        inactive.deactivate();
        productRepository.save(inactive);
        entityManager.flush();

        List<Product> activeProducts = productRepository.findAllActive();

        assertEquals(2, activeProducts.size());

        Set<String> skus = activeProducts.stream()
                        .map(p -> p.getSku().getValue())
                        .collect(Collectors.toSet());

        assertTrue(skus.contains("ACT-001"));
        assertTrue(skus.contains("ACT-002"));
        assertFalse(skus.contains("INACT-001"));
    }

    @Test
    void should_check_existence_by_sku() {
        productRepository.save(testProduct);
        entityManager.flush();

        assertTrue(productRepository.existsBySku(new Sku("TEST-001")));
        assertFalse(productRepository.existsBySku(new Sku("NON-EXISTENT")));
    }

}

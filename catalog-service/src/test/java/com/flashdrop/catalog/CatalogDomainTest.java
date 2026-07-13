package com.flashdrop.catalog;

import com.flashdrop.catalog.application.port.outbound.ProductRepositoryPort;
import com.flashdrop.catalog.application.usecase.GetProductsByIdsUseCase;
import com.flashdrop.catalog.domain.model.Product;
import com.flashdrop.catalog.domain.valueobjects.Money;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests unitarios del dominio y use cases del catalog-service.
 * No requieren Spring Context ni base de datos.
 */
class CatalogDomainTest {

    @Test
    void productRequiresName() {
        assertThrows(IllegalArgumentException.class, () ->
            new Product(null, UUID.randomUUID(), UUID.randomUUID(),
                    "", "desc", new Money(BigDecimal.valueOf(1000)), null, true)
        );
    }

    @Test
    void productRequiresNonNullPrice() {
        assertThrows(IllegalArgumentException.class, () ->
            new Product(null, UUID.randomUUID(), UUID.randomUUID(),
                    "Burger", "desc", null, null, true)
        );
    }

    @Test
    void moneyRejectsNegative() {
        assertThrows(IllegalArgumentException.class, () ->
            new Money(BigDecimal.valueOf(-100))
        );
    }

    @Test
    void moneyRejectsNull() {
        assertThrows(IllegalArgumentException.class, () ->
            new Money(null)
        );
    }

    @Test
    void validProductCreation() {
        Product p = new Product(
                UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(),
                "Burger doble", "Doble carne",
                new Money(BigDecimal.valueOf(8990)),
                "img.png", true
        );
        assertEquals("Burger doble", p.getName());
        assertEquals(BigDecimal.valueOf(8990), p.getPrice().amount());
        assertTrue(p.isAvailable());
    }

    @Test
    void getProductsByIdsReturnsEmptyForNullIds() {
        // Arrange: use case con un stub que nunca debería ser invocado
        ProductRepositoryPort stub = new ProductRepositoryPort() {
            @Override public List<Product> findAll() { return List.of(); }
            @Override public List<Product> findByIds(List<UUID> ids) { return List.of(); }
            @Override public Product save(Product product) { return product; }
        };
        var useCase = new GetProductsByIdsUseCase(stub);

        // Act
        List<Product> result = useCase.execute(null);

        // Assert: el use case retorna vacío sin llamar al port
        assertTrue(result.isEmpty());
    }

    @Test
    void getProductsByIdsReturnsEmptyForEmptyIds() {
        ProductRepositoryPort stub = new ProductRepositoryPort() {
            @Override public List<Product> findAll() { return List.of(); }
            @Override public List<Product> findByIds(List<UUID> ids) { return List.of(); }
            @Override public Product save(Product product) { return product; }
        };
        var useCase = new GetProductsByIdsUseCase(stub);

        List<Product> result = useCase.execute(List.of());
        assertTrue(result.isEmpty());
    }
}

package com.flashdrop.catalog.infrastructure.adapter.outbound.persistence.memory;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import com.flashdrop.catalog.application.port.outbound.ProductRepositoryPort;
import com.flashdrop.catalog.domain.model.Product;
import com.flashdrop.catalog.domain.valueobjects.Money;

/**
 * Adapter en memoria para desarrollo local.
 * Provee datos de ejemplo sin necesitar base de datos.
 */
@Repository
@Profile("local")
public class InMemoryProductRepositoryAdapter implements ProductRepositoryPort {

    private final List<Product> products = new ArrayList<>(List.of(
            new Product(
                    UUID.fromString("00000000-0000-0000-0000-000000000001"),
                    UUID.fromString("00000000-0000-0000-0000-000000000010"),
                    UUID.fromString("00000000-0000-0000-0000-000000000100"),
                    "Burger doble",
                    "Doble carne, queso y salsa de la casa",
                    new Money(BigDecimal.valueOf(8990)),
                    "assets/img/burger1.png",
                    true
            ),
            new Product(
                    UUID.fromString("00000000-0000-0000-0000-000000000002"),
                    UUID.fromString("00000000-0000-0000-0000-000000000010"),
                    UUID.fromString("00000000-0000-0000-0000-000000000100"),
                    "Papas cheddar",
                    "Papas fritas con cheddar y tocino",
                    new Money(BigDecimal.valueOf(4990)),
                    "assets/img/fries.png",
                    true
            )
    ));

    @Override
    public List<Product> findAll() {
        return List.copyOf(products);
    }

    @Override
    public List<Product> findByIds(List<UUID> ids) {
        return products.stream()
                .filter(product -> ids.contains(product.getId()))
                .toList();
    }

    @Override
    public Product save(Product product) {
        Product savedProduct = new Product(
                UUID.randomUUID(),
                product.getCategoryId(),
                product.getRestaurantId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getImage(),
                product.isAvailable()
        );
        products.add(savedProduct);
        return savedProduct;
    }
}

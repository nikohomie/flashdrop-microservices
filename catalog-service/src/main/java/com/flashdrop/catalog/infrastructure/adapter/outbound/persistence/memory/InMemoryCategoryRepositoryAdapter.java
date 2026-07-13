package com.flashdrop.catalog.infrastructure.adapter.outbound.persistence.memory;

import java.util.List;
import java.util.UUID;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import com.flashdrop.catalog.application.port.outbound.CategoryRepositoryPort;
import com.flashdrop.catalog.domain.model.Category;

/**
 * Adapter en memoria para categorías en desarrollo local.
 */
@Repository
@Profile("local")
public class InMemoryCategoryRepositoryAdapter implements CategoryRepositoryPort {

    private final List<Category> categories = List.of(
            new Category(UUID.fromString("00000000-0000-0000-0000-000000000010"), "Hamburguesas", "Combos y sandwiches", "assets/img/burger1.png"),
            new Category(UUID.fromString("00000000-0000-0000-0000-000000000020"), "Pizzas", "Pizzas familiares", "assets/img/pizza.png")
    );

    @Override
    public List<Category> findAll() {
        return categories;
    }
}

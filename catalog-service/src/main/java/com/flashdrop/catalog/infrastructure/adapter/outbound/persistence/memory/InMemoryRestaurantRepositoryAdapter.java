package com.flashdrop.catalog.infrastructure.adapter.outbound.persistence.memory;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import com.flashdrop.catalog.application.port.outbound.RestaurantRepositoryPort;
import com.flashdrop.catalog.domain.model.Restaurant;

/**
 * Adapter en memoria para restaurantes en desarrollo local.
 */
@Repository
@Profile("local")
public class InMemoryRestaurantRepositoryAdapter implements RestaurantRepositoryPort {

    private final List<Restaurant> restaurants = List.of(
            new Restaurant(
                    UUID.fromString("00000000-0000-0000-0000-000000000100"),
                    UUID.fromString("00000000-0000-0000-0000-000000001000"),
                    "Flash Restaurant Demo",
                    "Calle Falsa 123",
                    LocalDateTime.now()
            )
    );

    @Override
    public List<Restaurant> findAll() {
        return restaurants;
    }
}

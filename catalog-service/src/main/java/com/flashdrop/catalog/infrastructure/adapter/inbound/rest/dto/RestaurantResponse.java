package com.flashdrop.catalog.infrastructure.adapter.inbound.rest.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import com.flashdrop.catalog.domain.model.Restaurant;

/**
 * DTO de salida para restaurantes.
 */
public record RestaurantResponse(UUID id, UUID userId, String name, String address, LocalDateTime createdAt) {

    public static RestaurantResponse fromDomain(Restaurant restaurant) {
        return new RestaurantResponse(
                restaurant.getId(),
                restaurant.getUserId(),
                restaurant.getName(),
                restaurant.getAddress(),
                restaurant.getCreatedAt()
        );
    }
}

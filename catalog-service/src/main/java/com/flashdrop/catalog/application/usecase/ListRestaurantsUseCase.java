package com.flashdrop.catalog.application.usecase;

import java.util.List;

import org.springframework.stereotype.Service;

import com.flashdrop.catalog.application.port.outbound.RestaurantRepositoryPort;
import com.flashdrop.catalog.domain.model.Restaurant;

/**
 * Caso de uso: listar restaurantes del catálogo.
 */
@Service
public class ListRestaurantsUseCase {

    private final RestaurantRepositoryPort restaurantRepositoryPort;

    public ListRestaurantsUseCase(RestaurantRepositoryPort restaurantRepositoryPort) {
        this.restaurantRepositoryPort = restaurantRepositoryPort;
    }

    public List<Restaurant> execute() {
        return restaurantRepositoryPort.findAll();
    }
}

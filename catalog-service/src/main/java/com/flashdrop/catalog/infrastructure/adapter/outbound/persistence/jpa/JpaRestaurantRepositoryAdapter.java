package com.flashdrop.catalog.infrastructure.adapter.outbound.persistence.jpa;

import java.util.List;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import com.flashdrop.catalog.application.port.outbound.RestaurantRepositoryPort;
import com.flashdrop.catalog.domain.model.Restaurant;
import com.flashdrop.catalog.infrastructure.adapter.outbound.persistence.jpa.entity.RestaurantEntity;
import com.flashdrop.catalog.infrastructure.adapter.outbound.persistence.jpa.repository.SpringDataRestaurantRepository;

/**
 * Adapter JPA: implementa el puerto de restaurantes usando Spring Data contra PostgreSQL.
 */
@Repository
@Profile("!local")
public class JpaRestaurantRepositoryAdapter implements RestaurantRepositoryPort {

    private final SpringDataRestaurantRepository repository;

    public JpaRestaurantRepositoryAdapter(SpringDataRestaurantRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<Restaurant> findAll() {
        return repository.findAll()
                .stream()
                .map(RestaurantEntity::toDomain)
                .toList();
    }
}

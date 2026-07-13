package com.flashdrop.catalog.infrastructure.adapter.outbound.persistence.jpa.entity;

import java.time.LocalDateTime;
import java.util.UUID;

import com.flashdrop.catalog.domain.model.Restaurant;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * Entidad JPA mapeada a la tabla "restaurant" de PostgreSQL/Supabase.
 * Nota: la tabla se llama "restaurant" (singular) según el esquema legacy.
 */
@Entity
@Table(name = "restaurant")
public class RestaurantEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @Column(nullable = false)
    private String name;

    private String address;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    protected RestaurantEntity() {
    }

    public Restaurant toDomain() {
        return new Restaurant(id, userId, name, address, createdAt);
    }
}

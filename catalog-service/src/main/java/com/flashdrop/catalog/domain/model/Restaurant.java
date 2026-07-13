package com.flashdrop.catalog.domain.model;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Modelo de negocio: representa un restaurante registrado en la plataforma.
 */
public class Restaurant {

    private UUID id;
    private UUID userId;
    private String name;
    private String address;
    private LocalDateTime createdAt;

    public Restaurant(UUID id, UUID userId, String name, String address, LocalDateTime createdAt) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("El nombre del restaurante es obligatorio");
        }

        this.id = id;
        this.userId = userId;
        this.name = name;
        this.address = address;
        this.createdAt = createdAt;
    }

    public UUID getId() {
        return id;
    }

    public UUID getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}

package com.flashdrop.catalog.domain.model;

import java.util.UUID;

/**
 * Modelo de negocio: representa una categoría del catálogo (ej: Hamburguesas, Pizzas).
 */
public class Category {

    private UUID id;
    private String name;
    private String description;
    private String image;

    public Category(UUID id, String name, String description, String image) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("El nombre de la categoría es obligatorio");
        }

        this.id = id;
        this.name = name;
        this.description = description;
        this.image = image;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getImage() {
        return image;
    }
}

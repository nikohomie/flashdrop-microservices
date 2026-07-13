package com.flashdrop.catalog.domain.model;

import com.flashdrop.catalog.domain.valueobjects.Money;

import java.util.UUID;

/**
 * Modelo de negocio: representa un producto dentro del catálogo.
 * No sabe nada de HTTP, Supabase ni SQL; solo guarda datos y reglas propias.
 */
public class Product {

    private UUID id;
    private UUID categoryId;
    private UUID restaurantId;
    private String name;
    private String description;
    private Money price;
    private String image;
    private boolean available;

    public Product(
            UUID id,
            UUID categoryId,
            UUID restaurantId,
            String name,
            String description,
            Money price,
            String image,
            boolean available
    ) {
        // Regla de negocio: no dejamos crear productos sin nombre.
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("El nombre del producto es obligatorio");
        }

        // El precio viene como Money para concentrar la validación de monto ahí.
        if (price == null) {
            throw new IllegalArgumentException("El precio del producto no puede ser nulo");
        }

        this.id = id;
        this.categoryId = categoryId;
        this.restaurantId = restaurantId;
        this.name = name;
        this.description = description;
        this.price = price;
        this.image = image;
        this.available = available;
    }

    public UUID getId() {
        return id;
    }

    public UUID getCategoryId() {
        return categoryId;
    }

    public UUID getRestaurantId() {
        return restaurantId;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Money getPrice() {
        return price;
    }

    public String getImage() {
        return image;
    }

    public boolean isAvailable() {
        return available;
    }
}

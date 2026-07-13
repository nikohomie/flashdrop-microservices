package com.flashdrop.catalog.infrastructure.adapter.inbound.rest.dto;

import java.math.BigDecimal;
import java.util.UUID;

import com.flashdrop.catalog.domain.model.Product;

/**
 * DTO de salida: convierte el modelo interno Product al JSON que ve el cliente.
 */
public record ProductResponse(
        UUID id,
        UUID categoryId,
        UUID restaurantId,
        String name,
        String description,
        BigDecimal price,
        String image,
        boolean available
) {
    public static ProductResponse fromDomain(Product product) {
        return new ProductResponse(
                product.getId(),
                product.getCategoryId(),
                product.getRestaurantId(),
                product.getName(),
                product.getDescription(),
                product.getPrice().amount(),
                product.getImage(),
                product.isAvailable()
        );
    }
}

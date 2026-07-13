package com.flashdrop.catalog.infrastructure.adapter.inbound.rest.dto;

import java.util.UUID;

import com.flashdrop.catalog.domain.model.Category;

/**
 * DTO de salida para categorías.
 */
public record CategoryResponse(UUID id, String name, String description, String image) {

    public static CategoryResponse fromDomain(Category category) {
        return new CategoryResponse(
                category.getId(),
                category.getName(),
                category.getDescription(),
                category.getImage()
        );
    }
}

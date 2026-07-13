package com.flashdrop.catalog.infrastructure.adapter.inbound.rest.dto;

import java.math.BigDecimal;
import java.util.UUID;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

/**
 * DTO de entrada: representa el JSON que llega al hacer POST /catalog/products.
 * Incluye validaciones estrictas en la capa de infraestructura.
 */
public record CreateProductRequest(

        @NotNull(message = "El categoryId es obligatorio")
        UUID categoryId,

        @NotNull(message = "El restaurantId es obligatorio")
        UUID restaurantId,

        @NotBlank(message = "El nombre del producto es obligatorio")
        @Size(min = 2, max = 160, message = "El nombre debe tener entre 2 y 160 caracteres")
        String name,

        @Size(max = 500, message = "La descripción no puede superar 500 caracteres")
        String description,

        @NotNull(message = "El precio es obligatorio")
        @Positive(message = "El precio debe ser mayor a 0")
        BigDecimal price,

        String image,

        Boolean available
) {
}

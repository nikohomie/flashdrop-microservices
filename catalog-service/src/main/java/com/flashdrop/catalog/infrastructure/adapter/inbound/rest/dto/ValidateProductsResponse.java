package com.flashdrop.catalog.infrastructure.adapter.inbound.rest.dto;

import java.util.List;
import java.util.UUID;

/**
 * DTO de salida: indica cuáles productos existen y cuáles faltan.
 */
public record ValidateProductsResponse(
        boolean valid,
        List<ProductResponse> products,
        List<UUID> missingIds
) {
}

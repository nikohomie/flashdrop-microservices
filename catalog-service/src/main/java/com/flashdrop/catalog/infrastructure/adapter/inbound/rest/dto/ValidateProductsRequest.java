package com.flashdrop.catalog.infrastructure.adapter.inbound.rest.dto;

import java.util.List;
import java.util.UUID;

/**
 * DTO de entrada para validar productos desde otra lógica, como pedidos.
 */
public record ValidateProductsRequest(List<UUID> productIds) {
}

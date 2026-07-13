package com.flashdrop.catalog.application.usecase;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.flashdrop.catalog.application.port.outbound.ProductRepositoryPort;
import com.flashdrop.catalog.domain.model.Product;

/**
 * Caso de uso: buscar productos por una lista de UUIDs.
 * Sirve para que otra lógica (ej: pedidos) valide que los productos existen.
 */
@Service
public class GetProductsByIdsUseCase {

    private final ProductRepositoryPort productRepositoryPort;

    public GetProductsByIdsUseCase(ProductRepositoryPort productRepositoryPort) {
        this.productRepositoryPort = productRepositoryPort;
    }

    public List<Product> execute(List<UUID> ids) {
        if (ids == null || ids.isEmpty()) {
            return List.of();
        }
        return productRepositoryPort.findByIds(ids);
    }
}

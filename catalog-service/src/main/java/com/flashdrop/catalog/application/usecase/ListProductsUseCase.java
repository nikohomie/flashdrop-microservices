package com.flashdrop.catalog.application.usecase;

import java.util.List;

import org.springframework.stereotype.Service;

import com.flashdrop.catalog.application.port.outbound.ProductRepositoryPort;
import com.flashdrop.catalog.domain.model.Product;

/**
 * Caso de uso: listar todos los productos disponibles del catálogo.
 */
@Service
public class ListProductsUseCase {

    private final ProductRepositoryPort productRepositoryPort;

    public ListProductsUseCase(ProductRepositoryPort productRepositoryPort) {
        this.productRepositoryPort = productRepositoryPort;
    }

    public List<Product> execute() {
        return productRepositoryPort.findAll();
    }
}

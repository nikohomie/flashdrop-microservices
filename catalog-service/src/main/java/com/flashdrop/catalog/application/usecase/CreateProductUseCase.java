package com.flashdrop.catalog.application.usecase;

import org.springframework.stereotype.Service;

import com.flashdrop.catalog.application.port.outbound.ProductRepositoryPort;
import com.flashdrop.catalog.domain.model.Product;

/**
 * Caso de uso: crear un nuevo producto en el catálogo.
 */
@Service
public class CreateProductUseCase {

    private final ProductRepositoryPort productRepositoryPort;

    public CreateProductUseCase(ProductRepositoryPort productRepositoryPort) {
        this.productRepositoryPort = productRepositoryPort;
    }

    public Product execute(Product product) {
        // La validación principal vive en Product/Money; aquí coordinamos el guardado.
        return productRepositoryPort.save(product);
    }
}

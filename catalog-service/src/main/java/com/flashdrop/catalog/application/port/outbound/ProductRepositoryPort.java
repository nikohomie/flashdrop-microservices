package com.flashdrop.catalog.application.port.outbound;

import java.util.List;
import java.util.UUID;

import com.flashdrop.catalog.domain.model.Product;

/**
 * Puerto de salida: la aplicación declara qué operaciones necesita sobre productos.
 * No importa si la implementación real usa JPA, memoria o una API externa.
 */
public interface ProductRepositoryPort {

    /** Trae todos los productos del origen de datos activo. */
    List<Product> findAll();

    /** Busca productos por sus UUIDs (usado por validación de pedidos). */
    List<Product> findByIds(List<UUID> ids);

    /** Guarda un producto y devuelve el producto creado con su id real. */
    Product save(Product product);
}

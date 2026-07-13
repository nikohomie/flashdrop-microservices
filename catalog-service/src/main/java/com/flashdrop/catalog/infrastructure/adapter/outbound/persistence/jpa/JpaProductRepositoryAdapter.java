package com.flashdrop.catalog.infrastructure.adapter.outbound.persistence.jpa;

import java.util.List;
import java.util.UUID;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import com.flashdrop.catalog.application.port.outbound.ProductRepositoryPort;
import com.flashdrop.catalog.domain.model.Product;
import com.flashdrop.catalog.infrastructure.adapter.outbound.persistence.jpa.entity.ProductEntity;
import com.flashdrop.catalog.infrastructure.adapter.outbound.persistence.jpa.repository.SpringDataProductRepository;

/**
 * Adapter JPA: implementa el puerto de productos usando Spring Data contra PostgreSQL.
 * Activo en cualquier perfil excepto "local" (que usa InMemory).
 */
@Repository
@Profile("!local")
public class JpaProductRepositoryAdapter implements ProductRepositoryPort {

    private final SpringDataProductRepository repository;

    public JpaProductRepositoryAdapter(SpringDataProductRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<Product> findAll() {
        return repository.findAll()
                .stream()
                .map(ProductEntity::toDomain)
                .toList();
    }

    @Override
    public List<Product> findByIds(List<UUID> ids) {
        return repository.findByIdIn(ids)
                .stream()
                .map(ProductEntity::toDomain)
                .toList();
    }

    @Override
    public Product save(Product product) {
        ProductEntity entity = ProductEntity.fromDomain(product);
        ProductEntity saved = repository.save(entity);
        return saved.toDomain();
    }
}

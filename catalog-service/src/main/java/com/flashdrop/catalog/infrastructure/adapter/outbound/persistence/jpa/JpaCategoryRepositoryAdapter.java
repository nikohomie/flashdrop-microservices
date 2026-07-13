package com.flashdrop.catalog.infrastructure.adapter.outbound.persistence.jpa;

import java.util.List;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import com.flashdrop.catalog.application.port.outbound.CategoryRepositoryPort;
import com.flashdrop.catalog.domain.model.Category;
import com.flashdrop.catalog.infrastructure.adapter.outbound.persistence.jpa.entity.CategoryEntity;
import com.flashdrop.catalog.infrastructure.adapter.outbound.persistence.jpa.repository.SpringDataCategoryRepository;

/**
 * Adapter JPA: implementa el puerto de categorías usando Spring Data contra PostgreSQL.
 */
@Repository
@Profile("!local")
public class JpaCategoryRepositoryAdapter implements CategoryRepositoryPort {

    private final SpringDataCategoryRepository repository;

    public JpaCategoryRepositoryAdapter(SpringDataCategoryRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<Category> findAll() {
        return repository.findAll()
                .stream()
                .map(CategoryEntity::toDomain)
                .toList();
    }
}

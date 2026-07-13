package com.flashdrop.catalog.infrastructure.adapter.outbound.persistence.jpa.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.flashdrop.catalog.infrastructure.adapter.outbound.persistence.jpa.entity.CategoryEntity;

public interface SpringDataCategoryRepository extends JpaRepository<CategoryEntity, UUID> {
}

package com.flashdrop.catalog.infrastructure.adapter.outbound.persistence.jpa.repository;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.flashdrop.catalog.infrastructure.adapter.outbound.persistence.jpa.entity.ProductEntity;

public interface SpringDataProductRepository extends JpaRepository<ProductEntity, UUID> {

    List<ProductEntity> findByIdIn(Collection<UUID> ids);
}

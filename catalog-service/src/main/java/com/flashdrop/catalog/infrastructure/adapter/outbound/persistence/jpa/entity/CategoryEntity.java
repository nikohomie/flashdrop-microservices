package com.flashdrop.catalog.infrastructure.adapter.outbound.persistence.jpa.entity;

import java.util.UUID;

import com.flashdrop.catalog.domain.model.Category;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * Entidad JPA mapeada a la tabla "categories" de PostgreSQL/Supabase.
 */
@Entity
@Table(name = "categories")
public class CategoryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, unique = true, length = 100)
    private String name;

    private String description;

    private String image;

    protected CategoryEntity() {
    }

    public Category toDomain() {
        return new Category(id, name, description, image);
    }
}

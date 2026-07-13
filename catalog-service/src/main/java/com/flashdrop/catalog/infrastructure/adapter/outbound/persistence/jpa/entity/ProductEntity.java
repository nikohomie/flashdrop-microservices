package com.flashdrop.catalog.infrastructure.adapter.outbound.persistence.jpa.entity;

import java.math.BigDecimal;
import java.util.UUID;

import com.flashdrop.catalog.domain.model.Product;
import com.flashdrop.catalog.domain.valueobjects.Money;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * Entidad JPA mapeada a la tabla "products" de PostgreSQL/Supabase.
 * Los UUIDs se generan en la BD con gen_random_uuid().
 */
@Entity
@Table(name = "products")
public class ProductEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "category_id", nullable = false)
    private UUID categoryId;

    @Column(name = "restaurant_id", nullable = false)
    private UUID restaurantId;

    @Column(nullable = false, length = 160)
    private String name;

    private String description;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal price;

    private String image;

    @Column(name = "is_available", nullable = false)
    private boolean available;

    protected ProductEntity() {
        // Constructor vacío requerido por JPA
    }

    /** Crea una entidad JPA a partir de un modelo de dominio (para insert). */
    public static ProductEntity fromDomain(Product product) {
        ProductEntity entity = new ProductEntity();
        entity.id = product.getId(); // null en creación → la BD genera el UUID
        entity.categoryId = product.getCategoryId();
        entity.restaurantId = product.getRestaurantId();
        entity.name = product.getName();
        entity.description = product.getDescription();
        entity.price = product.getPrice().amount();
        entity.image = product.getImage();
        entity.available = product.isAvailable();
        return entity;
    }

    /** Convierte esta entidad JPA al modelo de dominio. */
    public Product toDomain() {
        return new Product(
                id,
                categoryId,
                restaurantId,
                name,
                description,
                new Money(price),
                image,
                available
        );
    }
}

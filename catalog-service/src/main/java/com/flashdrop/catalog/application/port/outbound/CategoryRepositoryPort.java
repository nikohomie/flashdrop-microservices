package com.flashdrop.catalog.application.port.outbound;

import java.util.List;

import com.flashdrop.catalog.domain.model.Category;

/**
 * Puerto para categorías: define lo que la lógica necesita, no cómo se obtiene.
 */
public interface CategoryRepositoryPort {

    List<Category> findAll();
}

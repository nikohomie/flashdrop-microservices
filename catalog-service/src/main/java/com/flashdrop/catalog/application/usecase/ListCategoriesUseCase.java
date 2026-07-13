package com.flashdrop.catalog.application.usecase;

import java.util.List;

import org.springframework.stereotype.Service;

import com.flashdrop.catalog.application.port.outbound.CategoryRepositoryPort;
import com.flashdrop.catalog.domain.model.Category;

/**
 * Caso de uso: listar categorías para filtros o formularios.
 */
@Service
public class ListCategoriesUseCase {

    private final CategoryRepositoryPort categoryRepositoryPort;

    public ListCategoriesUseCase(CategoryRepositoryPort categoryRepositoryPort) {
        this.categoryRepositoryPort = categoryRepositoryPort;
    }

    public List<Category> execute() {
        return categoryRepositoryPort.findAll();
    }
}

package com.flashdrop.catalog.infrastructure.adapter.inbound.rest;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.flashdrop.catalog.application.usecase.CreateProductUseCase;
import com.flashdrop.catalog.application.usecase.GetProductsByIdsUseCase;
import com.flashdrop.catalog.application.usecase.ListProductsUseCase;
import com.flashdrop.catalog.domain.model.Product;
import com.flashdrop.catalog.domain.valueobjects.Money;
import com.flashdrop.catalog.infrastructure.adapter.inbound.rest.dto.CreateProductRequest;
import com.flashdrop.catalog.infrastructure.adapter.inbound.rest.dto.ProductResponse;
import com.flashdrop.catalog.infrastructure.adapter.inbound.rest.dto.ValidateProductsRequest;
import com.flashdrop.catalog.infrastructure.adapter.inbound.rest.dto.ValidateProductsResponse;

import jakarta.validation.Valid;

/**
 * Controlador REST para la gestión de productos del catálogo.
 * GET es público; POST requiere JWT con rol Restaurante (configurado en SecurityConfig).
 */
@RestController
@RequestMapping("/catalog/products")
public class ProductController {

    private final ListProductsUseCase listProductsUseCase;
    private final GetProductsByIdsUseCase getProductsByIdsUseCase;
    private final CreateProductUseCase createProductUseCase;

    public ProductController(
            ListProductsUseCase listProductsUseCase,
            GetProductsByIdsUseCase getProductsByIdsUseCase,
            CreateProductUseCase createProductUseCase
    ) {
        this.listProductsUseCase = listProductsUseCase;
        this.getProductsByIdsUseCase = getProductsByIdsUseCase;
        this.createProductUseCase = createProductUseCase;
    }

    /** POST /catalog/products — crea un producto (requiere JWT). */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProductResponse createProduct(@Valid @RequestBody CreateProductRequest request) {
        Product product = new Product(
                null,
                request.categoryId(),
                request.restaurantId(),
                request.name(),
                request.description(),
                new Money(request.price()),
                request.image(),
                request.available() == null || request.available()
        );
        return ProductResponse.fromDomain(createProductUseCase.execute(product));
    }

    /** GET /catalog/products — lista productos (público). */
    @GetMapping
    public List<ProductResponse> listProducts() {
        return listProductsUseCase.execute()
                .stream()
                .map(ProductResponse::fromDomain)
                .toList();
    }

    /** POST /catalog/products/validate — valida existencia de productos por ids. */
    @PostMapping("/validate")
    public ValidateProductsResponse validateProducts(@RequestBody ValidateProductsRequest request) {
        List<UUID> requestedIds = request.productIds() == null ? List.of() : request.productIds();
        List<Product> products = getProductsByIdsUseCase.execute(requestedIds);
        List<UUID> foundIds = products.stream().map(Product::getId).toList();
        List<UUID> missingIds = requestedIds.stream()
                .filter(id -> !foundIds.contains(id))
                .toList();

        return new ValidateProductsResponse(
                missingIds.isEmpty(),
                products.stream().map(ProductResponse::fromDomain).toList(),
                missingIds
        );
    }
}

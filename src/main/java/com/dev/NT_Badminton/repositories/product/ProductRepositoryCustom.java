package com.dev.NT_Badminton.repositories.product;

import com.dev.NT_Badminton.dto.constant.ActiveStatus;
import com.dev.NT_Badminton.entities.products.Product;

import java.util.List;
import java.util.Optional;

public interface ProductRepositoryCustom {
    List<Product> getProducts(int page, String name, Integer categoryId, Boolean deleted, ActiveStatus status, Integer startPriceRange, Integer endPriceRange, String relatedProductSlug);
    long countProducts(String name, Integer categoryId, Boolean deleted, ActiveStatus status, Integer startPriceRange, Integer endPriceRange, String relatedProductSlug);
}

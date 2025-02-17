package com.dev.NT_Badminton.repositories.product;

import com.dev.NT_Badminton.entities.products.ProductVariants;

import java.util.List;
import java.util.Optional;

public interface ProductVariantRepositoryCustom {

    Optional<ProductVariants> findProductVariantByProductVariantOptionValuesIds(List<Integer> productVariantOptionValuesIds);
    Integer getMinProductVariantsPriceByProductId(Integer productId);
}

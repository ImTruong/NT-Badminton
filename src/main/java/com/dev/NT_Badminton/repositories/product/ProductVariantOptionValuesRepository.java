package com.dev.NT_Badminton.repositories.product;

import com.dev.NT_Badminton.entities.products.ProductVariantOptionValues;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductVariantOptionValuesRepository extends JpaRepository<ProductVariantOptionValues, Integer> {
}

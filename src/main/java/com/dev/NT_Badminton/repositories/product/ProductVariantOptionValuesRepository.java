package com.dev.NT_Badminton.repositories.product;

import com.dev.NT_Badminton.entities.products.ProductVariantOptionValues;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductVariantOptionValuesRepository extends JpaRepository<ProductVariantOptionValues, Integer> {

    List<ProductVariantOptionValues> findByProductVariantId(int productVariantId);

    void deleteAllByProductVariantId(int productVariantId);

}

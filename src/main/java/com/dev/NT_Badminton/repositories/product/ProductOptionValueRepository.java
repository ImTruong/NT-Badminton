package com.dev.NT_Badminton.repositories.product;

import com.dev.NT_Badminton.entities.products.ProductOptionValue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductOptionValueRepository extends JpaRepository<ProductOptionValue, Integer> {
    List<ProductOptionValue> findByProductOptionId(int productOptionId);
    List<ProductOptionValue> findByProductOptionIdAndDeleted(int productOptionId, boolean deleted);
    boolean existsByProductOptionIdAndDeleted(int productOptionId, boolean deleted);
}

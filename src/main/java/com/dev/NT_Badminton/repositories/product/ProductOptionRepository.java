package com.dev.NT_Badminton.repositories.product;

import com.dev.NT_Badminton.entities.products.ProductOption;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductOptionRepository extends JpaRepository<ProductOption, Integer> {
    List<ProductOption> findByProductIdAndDeleted(Integer productId, boolean deleted);

    List<ProductOption> findAllByProductId(Integer productId);

    boolean existsByProductIdAndOptionId(Integer productId, Integer optionId);
}

package com.dev.NT_Badminton.repositories.product;

import com.dev.NT_Badminton.entities.products.ProductOptionValue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductOptionValueRepository extends JpaRepository<ProductOptionValue, Integer> {

//    List<ProductOptionValue> findByProductOptionId(int productOptionId);
//
//    List<ProductOptionValue> findByProductOptionIdAndDeleted(int productOptionId, boolean deleted);
//
//    boolean existsByProductOptionIdAndDeleted(int productOptionId, boolean deleted);

    Optional<ProductOptionValue> findById(int ProductOptionValueId);

    ProductOptionValue findByIdAndDeleted(int id, boolean deleted);

}

package com.dev.NT_Badminton.repositories.product;

import com.dev.NT_Badminton.entities.products.ProductImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductImageRepository extends JpaRepository<ProductImage, Integer> {
    List<ProductImage> findAllByProductIdAndDeleted(int productId, boolean deleted);

    List<ProductImage> findAllByProductId(int productId);

    boolean existsByProductIdAndImageId(int productId, int imageId);

    Optional<ProductImage> findByProductIdAndImageId(Integer productId, Integer imageId);
}

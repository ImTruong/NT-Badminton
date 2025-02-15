package com.dev.NT_Badminton.repositories.product;

import com.dev.NT_Badminton.entities.products.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer>, ProductRepositoryCustom {
    Optional<Product> findBySlug(String slug);

    Optional<Product> findBySlugAndDeleted(String slug, boolean deleted);

    List<Product> findAllByCategoryIdAndDeleted(int categoryId, boolean deleted);
}

package com.dev.NT_Badminton.repositories.category;

import com.dev.NT_Badminton.entities.categories.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer>,CategoryRepositoryCustom {
    Optional<Category> findByIdAndDeleted(int id, boolean deleted);
}

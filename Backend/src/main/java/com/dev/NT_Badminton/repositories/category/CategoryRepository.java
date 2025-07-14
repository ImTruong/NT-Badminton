package com.dev.NT_Badminton.repositories.category;

import com.dev.NT_Badminton.entities.categories.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Integer>, CategoryRepositoryCustom {

}

package com.dev.NT_Badminton.repositories.category;

import com.dev.NT_Badminton.dto.response.category.CategoryResponse;
import com.dev.NT_Badminton.entities.categories.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Integer>, CategoryRepositoryCustom {


}

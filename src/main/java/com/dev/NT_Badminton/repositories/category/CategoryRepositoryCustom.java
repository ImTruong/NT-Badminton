package com.dev.NT_Badminton.repositories.category;

import com.dev.NT_Badminton.dto.constant.ActiveStatus;
import com.dev.NT_Badminton.entities.categories.Category;
import com.dev.NT_Badminton.entities.categories.constant.CategoryType;

import java.util.List;

public interface CategoryRepositoryCustom {
    List<Category> getCategories(String name, String slug, CategoryType type, ActiveStatus status, Boolean deleted);

    long countCategories(String name, String slug, CategoryType type, ActiveStatus status, Boolean deleted);
}

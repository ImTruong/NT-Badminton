package com.dev.NT_Badminton.repositories.category;

import com.dev.NT_Badminton.dto.response.category.CategoryResponse;

import java.util.List;

public interface CategoryRepositoryCustom {

    List<CategoryResponse> getAllCategories();

}

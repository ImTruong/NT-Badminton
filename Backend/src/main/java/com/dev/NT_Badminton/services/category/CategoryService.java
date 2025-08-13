package com.dev.NT_Badminton.services.category;

import com.dev.NT_Badminton.dto.request.category.ModifyCategoryRequest;
import com.dev.NT_Badminton.dto.response.category.CategoryResponse;

import java.util.List;

public interface CategoryService {

    void addOrUpdateCategory(ModifyCategoryRequest modifyCategoryRequest);

    void deleteCategory(int categoryId);

    List<CategoryResponse> getAllCategories();

    List<CategoryResponse> getRootCategories();
}

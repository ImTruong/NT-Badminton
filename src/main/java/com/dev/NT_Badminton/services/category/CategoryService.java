package com.dev.NT_Badminton.services.category;

import com.dev.NT_Badminton.dto.constant.ActiveStatus;
import com.dev.NT_Badminton.dto.request.IdsRequest;
import com.dev.NT_Badminton.dto.request.category.CreateCategoryReq;
import com.dev.NT_Badminton.dto.request.category.UpdateCategoryReq;
import com.dev.NT_Badminton.dto.response.ApiResponse;
import com.dev.NT_Badminton.entities.categories.Category;
import com.dev.NT_Badminton.entities.categories.constant.CategoryType;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CategoryService {
    ApiResponse<List<Category>> getCategories(String name, String slug, CategoryType type, ActiveStatus status, Boolean deleted) throws Exception;

    Category createCategory(CreateCategoryReq req) throws Exception;

    Category updateCategory(UpdateCategoryReq req) throws Exception;

    IdsRequest deleteCategories(IdsRequest req) throws Exception;

    IdsRequest restoreCategories(IdsRequest req) throws Exception;
}

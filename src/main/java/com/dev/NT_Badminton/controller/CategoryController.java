package com.dev.NT_Badminton.controller;

import com.cloudinary.Api;
import com.dev.NT_Badminton.dto.constant.ActiveStatus;
import com.dev.NT_Badminton.dto.request.IdsRequest;
import com.dev.NT_Badminton.dto.request.category.CreateCategoryReq;
import com.dev.NT_Badminton.dto.request.category.UpdateCategoryReq;
import com.dev.NT_Badminton.dto.response.ApiResponse;
import com.dev.NT_Badminton.entities.categories.Category;
import com.dev.NT_Badminton.entities.categories.constant.CategoryType;
import com.dev.NT_Badminton.services.category.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/category/")
public class CategoryController {
    private final CategoryService categoryService;

    @GetMapping("get-categories")
    public ResponseEntity<ApiResponse<List<Category>>> getCategoriesForUser(@RequestParam(required = false) String name,
                                                                            @RequestParam(required = false) CategoryType type) throws Exception {
        return ResponseEntity.ok(categoryService.getCategories(name,null, type, ActiveStatus.ACTIVE, false));
    }

    @GetMapping("admin/get-categories")
    public ResponseEntity<ApiResponse<List<Category>>> getCategoriesForAdmin(@RequestParam(required = false) String name,
                                                                             @RequestParam(required = false) String slug,
                                                                             @RequestParam(required = false) CategoryType type,
                                                                             @RequestParam(required = false) ActiveStatus status,
                                                                             @RequestParam(required = false) Boolean deleted) throws Exception {
        return ResponseEntity.ok(categoryService.getCategories(name,slug,type,status,deleted));
    }

    @PostMapping("admin/create-category")
    public ResponseEntity<ApiResponse<Category>> createCategory(@RequestBody @Valid CreateCategoryReq req) throws Exception {
        return ResponseEntity.ok(new ApiResponse<>(true,"Category created successfully!", categoryService.createCategory(req)));
    }

    @PutMapping("admin/update-category")
    public ResponseEntity<ApiResponse<Category>> updateCategory(@RequestBody @Valid UpdateCategoryReq req) throws Exception {
        return ResponseEntity.ok(new ApiResponse<>(true,"Category updated successfully!", categoryService.updateCategory(req)));
    }

    @PostMapping("admin/delete-categories")
    public ResponseEntity<ApiResponse<IdsRequest>> deleteCategories(@RequestBody @Valid IdsRequest req) throws Exception {
        return ResponseEntity.ok(new ApiResponse<>(true,"Category deleted successfully!", categoryService.deleteCategories(req)));
    }

    @PostMapping("admin/restore-categories")
    public ResponseEntity<ApiResponse<IdsRequest>> restoreCategories(@RequestBody @Valid IdsRequest req) throws Exception {
        return ResponseEntity.ok(new ApiResponse<>(true,"Category restored successfully!", categoryService.restoreCategories(req)));
    }
}

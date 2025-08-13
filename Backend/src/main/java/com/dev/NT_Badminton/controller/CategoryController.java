package com.dev.NT_Badminton.controller;

import com.dev.NT_Badminton.dto.request.category.ModifyCategoryRequest;
import com.dev.NT_Badminton.dto.response.ApiResponse;
import com.dev.NT_Badminton.services.category.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/category")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping
    public ResponseEntity<?> createCategory(@RequestBody @Valid ModifyCategoryRequest modifyCategoryRequest) {
        modifyCategoryRequest.setType("CREATE");
        categoryService.addOrUpdateCategory(modifyCategoryRequest);
        ApiResponse<String> response = new ApiResponse<String>(true, "Create Category Successful");
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<?> updateCategory(@RequestBody @Valid ModifyCategoryRequest modifyCategoryRequest) {
        modifyCategoryRequest.setType("UPDATE");
        categoryService.addOrUpdateCategory(modifyCategoryRequest);
        ApiResponse<String> response = new ApiResponse<String>(true, "Update Category Successful");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping
    public ResponseEntity<?> deleteCategory(@RequestParam Integer categoryId) {
        categoryService.deleteCategory(categoryId);
        ApiResponse<String> response = new ApiResponse<String>(true, "Delete Category Successful");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllCategories() {
        ApiResponse<?> response = new ApiResponse<>(true, "Get All Categories Successful", categoryService.getAllCategories());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/root")
    public ResponseEntity<?> getRootCategories() {
        ApiResponse<?> response = new ApiResponse<>(true, "Get Root Categories Successful", categoryService.getRootCategories());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}

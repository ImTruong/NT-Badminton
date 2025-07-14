package com.dev.NT_Badminton.services.category;

import com.dev.NT_Badminton.dto.request.category.ModifyCategoryRequest;
import com.dev.NT_Badminton.dto.response.category.CategoryResponse;
import com.dev.NT_Badminton.entities.categories.Category;
import com.dev.NT_Badminton.repositories.category.CategoryRepository;
import com.dev.NT_Badminton.services.uploadFile.UploadFileService;
import com.dev.NT_Badminton.util.Utils;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;
    private final UploadFileService uploadFileService;

    @Override
    public void addOrUpdateCategory(ModifyCategoryRequest modifyCategoryRequest) {
        Category category = "CREATE".equals(modifyCategoryRequest.getType())
                ? modelMapper.map(modifyCategoryRequest, Category.class)
                : categoryRepository.findById(modifyCategoryRequest.getId())
                .orElseThrow(() -> new IllegalArgumentException("Category not found"));
        if (modifyCategoryRequest.getImageId() !=null  && !uploadFileService.checkExistenceOfUploadFile(modifyCategoryRequest.getImageId()))
            throw new EntityNotFoundException("Image not found");
        modelMapper.map(modifyCategoryRequest, category);
        category.setSlug(Utils.removeCharacterVn(modifyCategoryRequest.getName()));
        categoryRepository.save(category);
    }

    @Override
    public void deleteCategory(int categoryId) {
        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new IllegalArgumentException("Category not found"));
        category.setDeleted(true);
        categoryRepository.save(category);
    }

    @Override
    public List<CategoryResponse> getAllCategories() {
        return categoryRepository.getAllCategories();
    }
}

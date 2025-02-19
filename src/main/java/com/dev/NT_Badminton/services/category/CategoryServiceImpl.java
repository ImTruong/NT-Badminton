package com.dev.NT_Badminton.services.category;

import com.dev.NT_Badminton.dto.constant.ActiveStatus;
import com.dev.NT_Badminton.dto.request.IdsRequest;
import com.dev.NT_Badminton.dto.request.category.CreateCategoryReq;
import com.dev.NT_Badminton.dto.request.category.UpdateCategoryReq;
import com.dev.NT_Badminton.dto.response.ApiResponse;
import com.dev.NT_Badminton.entities.blogs.Blog;
import com.dev.NT_Badminton.entities.categories.Category;
import com.dev.NT_Badminton.entities.categories.constant.CategoryType;
import com.dev.NT_Badminton.entities.products.Product;
import com.dev.NT_Badminton.repositories.blog.BlogRepository;
import com.dev.NT_Badminton.repositories.category.CategoryRepository;
import com.dev.NT_Badminton.repositories.product.ProductRepository;
import com.dev.NT_Badminton.services.blog.BlogService;
import com.dev.NT_Badminton.services.product.ProductService;
import com.dev.NT_Badminton.util.Utils;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final ProductService productService;
    private final ProductRepository productRepository;
    private final BlogRepository blogRepository;
    private final BlogService blogService;

    @Override
    public ApiResponse<List<Category>> getCategories(String name, String slug, CategoryType type, ActiveStatus status, Boolean deleted) throws Exception {
        List<Category> categories = categoryRepository.getCategories(name, slug, type, status, deleted);
        long count = categoryRepository.countCategories(name, slug, type, status, deleted);

        if(categories == null)
            throw new Exception("No categories found!");

        return new ApiResponse<>(true,categories,count);
    }

    @Transactional
    @Override
    public Category createCategory(CreateCategoryReq req) throws Exception {
        if(StringUtils.isEmpty(req.getName()))
            throw new Exception("Name is required!");

        String slug = Utils.removeCharacterVn(req.getName().trim()) + Utils.randomString(8);

    return categoryRepository.save(Category.builder()
                .name(req.getName().trim())
                .shortDescription(req.getShortDescription().trim())
                .imageId(req.getImageId())
                .slug(slug)
                .parentId(req.getParent_id())
                .type(req.getType())
                .build());
    }

    @Override
    public Category updateCategory(UpdateCategoryReq req) throws Exception {
        Category category = categoryRepository.findById(req.getId())
                .orElseThrow(() -> new Exception("Category not found!"));
        if(StringUtils.isNotEmpty(req.getName().trim()))
            category.setName(req.getName().trim());

        if(StringUtils.isNotEmpty(req.getShortDescription().trim()))
            category.setShortDescription(req.getShortDescription().trim());

        if(req.getType() != null)
            category.setType(req.getType());

        if(req.getStatus() != null)
            category.setStatus(req.getStatus());

        return categoryRepository.save(category);
    }

    @Override
    public IdsRequest deleteCategories(IdsRequest req) throws Exception {
        return handleCategories(req,true);
    }

    @Override
    public IdsRequest restoreCategories(IdsRequest req) throws Exception {
        return handleCategories(req,false);
    }

    @NotNull
    private IdsRequest handleCategories(IdsRequest req,boolean deleted) throws Exception {
        List<Category> categoryList = new ArrayList<>();

        if(req.getIds() != null){
            for(Integer id : req.getIds()){
                Category category = categoryRepository.findById(id)
                        .orElseThrow(() -> new Exception("Category not found!"));

                categoryList.add(category);
            }
        }

        if(!categoryList.isEmpty()) {
            categoryList.forEach(category -> {
                category.setDeleted(deleted);

                switch (category.getType()) {
                    case BLOG -> {
                        List<Blog> blogList = blogRepository.findAllByCategoryIdAndDeleted(category.getId(),false );
                        List<Integer> blogIds = new ArrayList<>();
                        IdsRequest blogIdsRequest = new IdsRequest();

                        if(blogList != null && !blogList.isEmpty()){
                            blogList.forEach(blog -> {
                                blogIds.add(blog.getId());
                            });
                        }

                        if(!blogIds.isEmpty()) {
                            blogIdsRequest.setIds(blogIds);
                            try {
                                if(deleted) {
                                    blogService.deleteBlogs(blogIdsRequest);
                                }
                                else {
                                    blogService.restoreBlogs(blogIdsRequest);
                                }
                            } catch (Exception e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }
                    case PRODUCT -> {
                        List<Product> productList = productRepository.findAllByCategoryIdAndDeleted(category.getId(),false );
                        List<Integer> productIds = new ArrayList<>();
                        IdsRequest productIdsRequest = new IdsRequest();

                        if(productList != null && !productList.isEmpty()){
                            productList.forEach(product -> {
                                productIds.add(product.getId());
                            });
                        }

                        if(!productIds.isEmpty()) {
                            productIdsRequest.setIds(productIds);
                            try {
                                if(deleted) {
                                    productService.deleteProducts(productIdsRequest);
                                }
                                else {
                                    productService.restoreProducts(productIdsRequest);
                                }
                            } catch (Exception e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }
                }
            });
            categoryRepository.saveAll(categoryList);
        }
        return req;
    }

}

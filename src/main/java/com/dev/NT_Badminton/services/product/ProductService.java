package com.dev.NT_Badminton.services.product;

import com.dev.NT_Badminton.dto.constant.ActiveStatus;
import com.dev.NT_Badminton.dto.request.IdsRequest;
import com.dev.NT_Badminton.dto.request.product.CreateProductRequest;
import com.dev.NT_Badminton.dto.request.product.UpdateProductRequest;
import com.dev.NT_Badminton.dto.response.ApiResponse;
import com.dev.NT_Badminton.entities.products.Product;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ProductService {
    ApiResponse<List<Product>> getProducts(int page, String name, Integer categoryId, Boolean deleted, ActiveStatus status, Integer startPriceRange, Integer endPriceRange);

    ApiResponse<List<Product>> getRelatedProducts(int page, String slug);

    Product getProductDetail(String slug, boolean isForAdmin) throws Exception;

    Product createProduct(CreateProductRequest req) throws Exception;

    Product updateProduct(UpdateProductRequest req) throws Exception;

    IdsRequest deleteProduct(IdsRequest req) throws Exception;

    IdsRequest restoreProduct(IdsRequest req) throws Exception;

}

package com.dev.NT_Badminton.repositories.product;

import com.dev.NT_Badminton.dto.request.product.SearchProductRequest;
import com.dev.NT_Badminton.dto.response.product.ProductDetailResponse;
import com.dev.NT_Badminton.dto.response.product.SearchProductReponse;
import com.dev.NT_Badminton.entities.products.Product;

import java.util.List;
import java.util.Optional;

public interface ProductRepositoryCustom {

    List<Integer> findProductIdsByOptionValueIds(List<Integer> productOptionValueIds, int productId);

    List<SearchProductReponse> findProducts(SearchProductRequest searchProductRequest);

    ProductDetailResponse findProductDetailById(int productId);
}

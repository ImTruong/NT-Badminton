package com.dev.NT_Badminton.repositories.product;

import com.dev.NT_Badminton.dto.request.product.SearchProductRequest;
import com.dev.NT_Badminton.dto.response.product.ProductDetailResponse;
import com.dev.NT_Badminton.dto.response.product.SearchProductReponse;
import com.dev.NT_Badminton.entities.products.Product;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface ProductRepositoryCustom {

    List<Integer> findProductIdsByOptionValueIds(List<Integer> productOptionValueIds, int productId);

    PageImpl<SearchProductReponse> findProducts(SearchProductRequest searchProductRequest, Pageable pageable);

    ProductDetailResponse findProductDetailById(int productId);

    List<String> getAllProductBrands();
}

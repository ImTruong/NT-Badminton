package com.dev.NT_Badminton.services.product;

import com.cloudinary.Api;
import com.dev.NT_Badminton.dto.constant.ActiveStatus;
import com.dev.NT_Badminton.dto.request.IdsRequest;
import com.dev.NT_Badminton.dto.request.product.CreateProductRequest;
import com.dev.NT_Badminton.dto.request.product.UpdateProductRequest;
import com.dev.NT_Badminton.dto.response.ApiResponse;
import com.dev.NT_Badminton.entities.products.Product;
import com.dev.NT_Badminton.entities.products.ProductOption;
import com.dev.NT_Badminton.entities.products.ProductOptionValue;
import com.dev.NT_Badminton.entities.products.ProductVariants;

import java.util.List;
import java.util.Optional;

public interface ProductService {

    Product getProductById(int productId);

    ProductOption getProductOptionById(int productOptionId);

    ProductOptionValue getProductOptionValueById(int productOptionValueId);

    boolean checkIfProductHasOption(int productId);

    ProductVariants getProductVariantOfNonOptionedProduct(int productId);

    ProductVariants getProductVariantByProductOptionValueIds(List<Integer> productOptionValueIds);

    void ReduceQuantityOfProductVariant(ProductVariants productVariant, int quantity);

    Product getProductDetail(String slug, boolean isForAdmin) throws Exception;

    Product createProduct(CreateProductRequest req) throws Exception;

    Product updatePoduct(UpdateProductRequest req) throws Exception;

    IdsRequest deleteProducts(IdsRequest req) throws Exception;

    IdsRequest restoreProducts(IdsRequest req) throws Exception;

    ApiResponse<List<Product>> getProducts(int page, String name, Integer categoryId , Boolean deleted, ActiveStatus status, Integer startPriceRange, Integer endPriceRange) throws Exception;

    ApiResponse<List<Product>> getRelatedProductList(int page, String slug) throws Exception;
}

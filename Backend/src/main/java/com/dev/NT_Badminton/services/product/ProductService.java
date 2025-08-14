package com.dev.NT_Badminton.services.product;

import com.dev.NT_Badminton.dto.request.product.*;
import com.dev.NT_Badminton.dto.response.product.ProductDetailResponse;
import com.dev.NT_Badminton.dto.response.product.SearchProductReponse;
import com.dev.NT_Badminton.entities.products.Product;
import com.dev.NT_Badminton.entities.products.ProductOption;
import com.dev.NT_Badminton.entities.products.ProductOptionValue;
import com.dev.NT_Badminton.entities.products.ProductVariants;
import com.dev.NT_Badminton.entities.products.constant.ProductImageType;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


public interface ProductService {

    Product getProductById(int productId);

    ProductOption getProductOptionById(int productOptionId);

    ProductOptionValue getProductOptionValueById(int productOptionValueId);

    boolean checkIfProductHasOption(int productId);

    ProductVariants getProductVariantOfNonOptionedProduct(int productId);

    ProductVariants getProductVariantByProductOptionValueIds(List<Integer> productOptionValueIds);

    ProductVariants getProductVariantById(int productVariantId);

    void changeQuantityOfProductDueToOrderAct(Integer orderId, String orderType);

    void addOrUpdateProduct(ModifyProductRequest modifyProductRequest);

    void addOrUpdateProductOption(ModifyProductOptionRequest modifyProductOptionRequest);

    void addOrUpdateProductOptionValue(ModifyProductOptionValueRequest modifyProductOptionValueRequest);

    void addOrUpdateProductVariant(ModifyProductVariantRequest modifyProductVariantRequest);

    void deleteProduct(int productId);

    void deleteProductOption(int productOptionId);

    void deleteProductOptionValue(int productOptionValueId);

    void deleteProductVariant(int productVariantId);

    PageImpl<SearchProductReponse> searchProducts(SearchProductRequest searchProductRequest, Pageable pageable);

    ProductDetailResponse getProductDetail(int productId);

    boolean addProductImage(int productId, MultipartFile image, ProductImageType imageType) throws Exception;

    void deleteProductImage(int imageId) throws Exception;

    List<String> getAllProductBrands();
}

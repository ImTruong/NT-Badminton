package com.dev.NT_Badminton.services.product;

import com.dev.NT_Badminton.entities.products.Product;
import com.dev.NT_Badminton.entities.products.ProductOption;
import com.dev.NT_Badminton.entities.products.ProductOptionValue;
import com.dev.NT_Badminton.entities.products.ProductVariants;

import java.util.List;


public interface ProductService {

    Product getProductById(int productId);

    ProductOption getProductOptionById(int productOptionId);

    ProductOptionValue getProductOptionValueById(int productOptionValueId);

    boolean checkIfProductHasOption(int productId);

    ProductVariants getProductVariantOfNonOptionedProduct(int productId);

    ProductVariants getProductVariantByProductOptionValueIds(List<Integer> productOptionValueIds);

    ProductVariants getProductVariantById(int productVariantId);

    void ReduceQuantityOfProductVariant(ProductVariants productVariant, int quantity);

}

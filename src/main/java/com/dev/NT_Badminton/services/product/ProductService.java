package com.dev.NT_Badminton.services.product;

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

}

package com.dev.NT_Badminton.services.product;

import com.dev.NT_Badminton.entities.products.Product;
import com.dev.NT_Badminton.entities.products.ProductOption;
import com.dev.NT_Badminton.entities.products.ProductOptionValue;

public interface ProductService {

    Product getProductById(int productId);

    ProductOption getProductOptionById(int productOptionId);

    ProductOptionValue getProductOptionValueById(int productOptionValueId);

}

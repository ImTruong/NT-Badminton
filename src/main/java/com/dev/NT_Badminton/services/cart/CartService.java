package com.dev.NT_Badminton.services.cart;

import com.dev.NT_Badminton.dto.request.cart.AddProductToCartRequest;
import com.dev.NT_Badminton.dto.request.cart.QuantityChangeRequest;

public interface CartService {

    void addProductToCart(AddProductToCartRequest addProductToCartRequest);

    void changeProductQuantity(QuantityChangeRequest quantityChangeRequest);

    void deleteProductFromCart(Integer productVariantId);

}

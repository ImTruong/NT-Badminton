package com.dev.NT_Badminton.services.cart;

import com.dev.NT_Badminton.dto.request.cart.AddToCartRequest;

import java.util.List;

public interface CartService {

    void addProductToCart(AddToCartRequest addToCartRequest);

}

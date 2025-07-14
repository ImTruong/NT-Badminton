package com.dev.NT_Badminton.services.cart;

import com.dev.NT_Badminton.dto.request.cart.AddProductToCartRequest;
import com.dev.NT_Badminton.dto.request.cart.QuantityChangeRequest;
import com.dev.NT_Badminton.dto.response.cart.CartProductResponse;
import com.dev.NT_Badminton.entities.carts.Cart;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CartService {

    Cart addProductToCart(AddProductToCartRequest addProductToCartRequest);

    Cart changeProductQuantity(QuantityChangeRequest quantityChangeRequest);

    void deleteProductFromCart(Integer productVariantId);

    void deleteAllProductFromCart();

    public PageImpl<CartProductResponse> getUserCart(Pageable pageable);

    Cart checkProductExistenceInUserCart(Integer productVariantId, Integer userId);

}

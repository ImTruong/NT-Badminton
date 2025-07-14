package com.dev.NT_Badminton.repositories.cart;

import com.dev.NT_Badminton.dto.response.cart.CartProductResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CartRepositoryCustom {

    PageImpl<CartProductResponse> getCartProductResponseByUserId(Integer userId, Pageable pageable) ;
}

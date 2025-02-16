package com.dev.NT_Badminton.controller;

import com.dev.NT_Badminton.dto.request.cart.AddToCartRequest;
import com.dev.NT_Badminton.dto.response.ApiResponse;
import com.dev.NT_Badminton.services.cart.CartService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/cart")
public class CartController {

    @Autowired
    CartService cartService;

    @PostMapping("/add")
    public ResponseEntity<?> addCart(@RequestBody @Valid AddToCartRequest addToCartRequest) {
        cartService.addProductToCart(addToCartRequest);
        ApiResponse<String> response = new ApiResponse<String>(true,"Add Cart Successful");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


}

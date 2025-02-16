package com.dev.NT_Badminton.controller;

import com.dev.NT_Badminton.dto.request.cart.AddProductToCartRequest;
import com.dev.NT_Badminton.dto.request.cart.QuantityChangeRequest;
import com.dev.NT_Badminton.dto.response.ApiResponse;
import com.dev.NT_Badminton.services.cart.CartService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cart")
public class CartController {

    @Autowired
    CartService cartService;

    @PostMapping("/add")
    public ResponseEntity<?> addCart(@RequestBody @Valid AddProductToCartRequest addProductToCartRequest) {
        cartService.addProductToCart(addProductToCartRequest);
        ApiResponse<String> response = new ApiResponse<String>(true,"Add Cart Successful");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/update-quantity")
    public ResponseEntity<?> updateQuantity(@RequestBody @Valid QuantityChangeRequest quantityChangeRequest) {
        cartService.changeProductQuantity(quantityChangeRequest);
        ApiResponse<String> response = new ApiResponse<String>(true,"Update Quantity Successful");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteCart(@RequestParam Integer productVariantId) {
        cartService.deleteProductFromCart(productVariantId);
        ApiResponse<String> response = new ApiResponse<String>(true,"Delete From Cart Successful");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}

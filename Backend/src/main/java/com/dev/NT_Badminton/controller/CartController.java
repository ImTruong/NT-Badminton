package com.dev.NT_Badminton.controller;

import com.dev.NT_Badminton.dto.request.cart.AddProductToCartRequest;
import com.dev.NT_Badminton.dto.request.cart.QuantityChangeRequest;
import com.dev.NT_Badminton.dto.response.ApiResponse;
import com.dev.NT_Badminton.dto.response.cart.CartProductResponse;
import com.dev.NT_Badminton.services.cart.CartService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cart")
public class CartController {

    @Autowired
    CartService cartService;

    @PostMapping
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

    @DeleteMapping("/delete-all")
    public ResponseEntity<?> deleteAllCart() {
        cartService.deleteAllProductFromCart();
        ApiResponse<String> response = new ApiResponse<String>(true,"Delete All From Cart Successful");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<?> getAllCart(Pageable pageable) {
        ApiResponse<?> response = new ApiResponse<>(true, "Get All Cart Successful", cartService.getUserCart(pageable));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }



}

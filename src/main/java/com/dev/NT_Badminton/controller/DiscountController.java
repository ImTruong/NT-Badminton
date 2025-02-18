package com.dev.NT_Badminton.controller;

import com.dev.NT_Badminton.dto.request.discount.CreateDiscountRequest;
import com.dev.NT_Badminton.dto.response.ApiResponse;
import com.dev.NT_Badminton.services.discount.DiscountService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/discount")
public class DiscountController {

    @Autowired
    DiscountService discountService;

    @PostMapping
    public ResponseEntity<?> createDiscount(@RequestBody @Valid CreateDiscountRequest createDiscountRequest) {
        discountService.createDiscount(createDiscountRequest);
        ApiResponse<String> response = new ApiResponse<String>(true, "Create Discount Successful");
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }




}

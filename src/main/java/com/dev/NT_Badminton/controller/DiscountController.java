package com.dev.NT_Badminton.controller;

import com.dev.NT_Badminton.dto.request.discount.ModifyDiscountRequest;
import com.dev.NT_Badminton.dto.response.ApiResponse;
import com.dev.NT_Badminton.services.discount.DiscountService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/discount")
public class DiscountController {

    @Autowired
    DiscountService discountService;

    @PostMapping
    public ResponseEntity<?> createDiscount(@RequestBody @Valid ModifyDiscountRequest modifyDiscountRequest) {
        modifyDiscountRequest.setType("CREATE");
        discountService.changeOrAddDiscount(modifyDiscountRequest);
        ApiResponse<String> response = new ApiResponse<String>(true, "Create Discount Successful");
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<?> updateDiscount(@RequestBody ModifyDiscountRequest modifyDiscountRequest) {
        modifyDiscountRequest.setType("UPDATE");
        discountService.changeOrAddDiscount(modifyDiscountRequest);
        ApiResponse<String> response = new ApiResponse<String>(true, "Update Discount Successful");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping
    public ResponseEntity<?> deleteDiscount(@RequestParam Integer discountId) {
        discountService.deleteDiscount(discountId);
        ApiResponse<String> response = new ApiResponse<String>(true, "Delete Discount Successful");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


}

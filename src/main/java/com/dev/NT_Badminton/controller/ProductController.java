package com.dev.NT_Badminton.controller;

import com.dev.NT_Badminton.dto.request.IdsRequest;
import com.dev.NT_Badminton.dto.request.product.CreateProductRequest;
import com.dev.NT_Badminton.dto.request.product.UpdateProductRequest;
import com.dev.NT_Badminton.dto.response.ApiResponse;
import com.dev.NT_Badminton.entities.products.Product;
import com.dev.NT_Badminton.services.product.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/product/")
public class ProductController {
    private final ProductService productService;
    @PostMapping("admin/create-product")
    public ResponseEntity<ApiResponse<Product>> createProduct(@RequestBody @Valid CreateProductRequest req) throws Exception {
        return ResponseEntity.ok(new ApiResponse<>(true,"Create Product Successfully!",productService.createProduct(req)));
    }
    @PutMapping("admin/update-product")
    public ResponseEntity<ApiResponse<Product>> updateProduct(@RequestBody @Valid UpdateProductRequest req) throws Exception {
        return ResponseEntity.ok(new ApiResponse<>(true,"Update product successfully!",productService.updatePoduct(req)));
    }

    @PostMapping("admin/delete-products")
    public ResponseEntity<ApiResponse<IdsRequest>> deleteProducts(@RequestBody @Valid IdsRequest req) throws Exception {
        return ResponseEntity.ok(new ApiResponse<>(true, "Delete products successfully!",productService.deleteProducts(req)));
    }

    @PostMapping("admin/restore-products")
    public ResponseEntity<ApiResponse<IdsRequest>> restoreProducts(@RequestBody @Valid IdsRequest req) throws Exception {
        return ResponseEntity.ok(new ApiResponse<>(true, "Restore products successfully!",productService.restoreProducts(req)));
    }

}

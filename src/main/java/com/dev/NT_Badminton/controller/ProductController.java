package com.dev.NT_Badminton.controller;

import com.dev.NT_Badminton.dto.request.product.*;
import com.dev.NT_Badminton.dto.response.ApiResponse;
import com.dev.NT_Badminton.entities.products.constant.ProductImageType;
import com.dev.NT_Badminton.entities.upload_file.UploadFile;
import com.dev.NT_Badminton.services.product.ProductService;
import com.dev.NT_Badminton.services.uploadFile.UploadFileService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/product")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;
    private final UploadFileService uploadFileService;

    @PostMapping
    public ResponseEntity<?> createProduct(@RequestBody @Valid ModifyProductRequest modifyProductRequest) {
        modifyProductRequest.setType("CREATE");
        productService.addOrUpdateProduct(modifyProductRequest);
        ApiResponse<String> response = new ApiResponse<String>(true, "Product Created Successfully");
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<?> updateProduct(@RequestBody @Valid ModifyProductRequest modifyProductRequest) {
        modifyProductRequest.setType("UPDATE");
        productService.addOrUpdateProduct(modifyProductRequest);
        ApiResponse<String> response = new ApiResponse<String>(true, "Product Updated Successfully");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/option")
    public ResponseEntity<?> createProductOption(@RequestBody @Valid ModifyProductOptionRequest modifyProductOptionRequest) {
        modifyProductOptionRequest.setType("CREATE");
        productService.addOrUpdateProductOption(modifyProductOptionRequest);
        ApiResponse<String> response = new ApiResponse<String>(true, "Product Option Created Successfully");
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/option")
    public ResponseEntity<?> updateProductOption(@RequestBody @Valid ModifyProductOptionRequest modifyProductOptionRequest) {
        modifyProductOptionRequest.setType("UPDATE");
        productService.addOrUpdateProductOption(modifyProductOptionRequest);
        ApiResponse<String> response = new ApiResponse<String>(true, "Product Option Updated Successfully");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/optionValue")
    public ResponseEntity<?> createProductOptionValue(@RequestBody @Valid ModifyProductOptionValueRequest modifyProductOptionValueRequest) {
        modifyProductOptionValueRequest.setType("CREATE");
        productService.addOrUpdateProductOptionValue(modifyProductOptionValueRequest);
        ApiResponse<String> response = new ApiResponse<String>(true, "Product Option Value Created Successfully");
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/optionValue")
    public ResponseEntity<?> updateProductOptionValue(@RequestBody @Valid ModifyProductOptionValueRequest modifyProductOptionValueRequest) {
        modifyProductOptionValueRequest.setType("UPDATE");
        productService.addOrUpdateProductOptionValue(modifyProductOptionValueRequest);
        ApiResponse<String> response = new ApiResponse<String>(true, "Product Option Value Updated Successfully");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/variant")
    public ResponseEntity<?> createProductVariant(@RequestBody @Valid ModifyProductVariantRequest modifyProductVariantRequest) {
        modifyProductVariantRequest.setType("CREATE");
        productService.addOrUpdateProductVariant(modifyProductVariantRequest);
        ApiResponse<String> response = new ApiResponse<String>(true, "Product Variant Created Successfully");
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/variant")
    public ResponseEntity<?> updateProductVariant(@RequestBody @Valid ModifyProductVariantRequest modifyProductVariantRequest) {
        modifyProductVariantRequest.setType("UPDATE");
        productService.addOrUpdateProductVariant(modifyProductVariantRequest);
        ApiResponse<String> response = new ApiResponse<String>(true, "Product Variant Updated Successfully");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping()
    public ResponseEntity<?> searchProducts(@RequestBody @Valid SearchProductRequest searchProductRequest) {
        ApiResponse<?> response = new ApiResponse<>(true,"Search result fetch successfully" ,productService.searchProducts(searchProductRequest));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/{productId}")
    public ResponseEntity<?> getProductDetail(@PathVariable int productId) {
        ApiResponse<?> response = new ApiResponse<>(true,"Product detail fetch successfully" ,productService.getProductDetail(productId));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<?> deleteProduct(@PathVariable int productId) {
        productService.deleteProduct(productId);
        ApiResponse<String> response = new ApiResponse<>(true,"Product deleted successfully");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/option/{optionId}")
    public ResponseEntity<?> deleteProductOption(@PathVariable int optionId) {
        productService.deleteProductOption(optionId);
        ApiResponse<String> response = new ApiResponse<>(true,"Product option deleted successfully");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/optionValue/{optionValueId}")
    public ResponseEntity<?> deleteProductOptionValue(@PathVariable int optionValueId) {
        productService.deleteProductOptionValue(optionValueId);
        ApiResponse<String> response = new ApiResponse<>(true,"Product option value deleted successfully");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/variant/{variantId}")
    public ResponseEntity<?> deleteProductVariant(@PathVariable int variantId) {
        productService.deleteProductVariant(variantId);
        ApiResponse<String> response = new ApiResponse<>(true,"Product variant deleted successfully");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/image")
    public ResponseEntity<?> uploadProductImage(@RequestPart("file") MultipartFile file,
                                                @RequestParam("productId") int productId,
                                                @RequestParam("type") ProductImageType type) throws Exception {
        productService.addProductImage(productId, file, type);
        ApiResponse<?> response = new ApiResponse<>(true,"Product image uploaded successfully");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/image")
    public ResponseEntity<?> deleteProductImage(@RequestParam int imageId) throws Exception {
        productService.deleteProductImage(imageId);
        ApiResponse<String> response = new ApiResponse<>(true,"Product image deleted successfully");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}

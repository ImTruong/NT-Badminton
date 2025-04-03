package com.dev.NT_Badminton.dto.response.product;

import com.dev.NT_Badminton.dto.response.rating.RatingResponse;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProductDetailResponse {

    Integer id;

    String name;

    String brand;

    String shortDescription;

    String description;

    Integer categoryId;

    String mainImageUrl;

    String coverImageUrl;

    List<String> imageUrls;

    List<RatingResponse> ratings;

    List<ProductOptionResponse> options;

    List<ProductVariantResponse> variants;

    public ProductDetailResponse(Integer id, String name, String brand, String shortDescription, String description, Integer categoryId) {
        this.id = id;
        this.name = name;
        this.brand = brand;
        this.shortDescription = shortDescription;
        this.description = description;
        this.categoryId = categoryId;
    }
}

package com.dev.NT_Badminton.dto.response.product;

import com.dev.NT_Badminton.entities.products.constant.ProductImageType;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProductImageResponse {
    Integer productImageId;

    String imageUrl;

    ProductImageType type;


}

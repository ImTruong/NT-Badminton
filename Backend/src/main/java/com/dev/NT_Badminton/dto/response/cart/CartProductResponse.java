package com.dev.NT_Badminton.dto.response.cart;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CartProductResponse {

    Integer productId;

    Integer productVariantId;

    Integer quantity;

    String productName;

    Map<String,String> productOptionalValue;

    String productCoverImage;

    Double originalPrice;

    Double salePrice;

    public CartProductResponse(Integer productId,Integer productVariantId, Integer quantity, String productName, String productCoverImage, Double originalPrice) {
        this.productId = productId;
        this.productVariantId = productVariantId;
        this.quantity = quantity;
        this.productName = productName;
        this.productCoverImage = productCoverImage;
        this.originalPrice = originalPrice;
    }

}

package com.dev.NT_Badminton.dto.response.order;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrderItemResponse {

    Integer productVariantId;

    String productName;

    Map<String,String> productOptions;

    Integer quantity;

    Double price;

    Double priceAfterDiscount;

    String image;

    public OrderItemResponse(Integer productVariantId,String image, Double price, String productName, Integer quantity) {
        this.productVariantId = productVariantId;
        this.image = image;
        this.price = price;
        this.productName = productName;
        this.quantity = quantity;
    }
}

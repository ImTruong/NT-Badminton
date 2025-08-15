package com.dev.NT_Badminton.dto.response.product;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)

public class ProductVariantResponse {

    Integer id;

    String sku;

    Double price;

    Double priceAfterDiscount;

    Integer stock;

    Map<Integer,Integer> optionValues;

    public ProductVariantResponse(Integer id, String sku, Double price, Double priceAfterDiscount, Integer stock) {
        this.id = id;
        this.sku = sku;
        this.price = price;
        this.priceAfterDiscount = priceAfterDiscount;
        this.stock = stock;
    }
}

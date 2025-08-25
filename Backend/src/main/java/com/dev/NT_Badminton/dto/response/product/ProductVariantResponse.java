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

    Double priceBeforeDiscount;

    Double price;

    Integer stock;

    Map<Integer,Integer> optionValues;

    public ProductVariantResponse(Integer id, String sku, Double priceBeforeDiscount, Double price, Integer stock) {
        this.id = id;
        this.sku = sku;
        this.priceBeforeDiscount = priceBeforeDiscount;
        this.price = price;
        this.stock = stock;
    }
}

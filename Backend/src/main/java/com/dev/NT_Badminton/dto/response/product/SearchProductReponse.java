package com.dev.NT_Badminton.dto.response.product;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SearchProductReponse {

    Integer id;

    String name;

    String brand;

    String mainImageUrl;

    Double rating;

    Double priceBeforeDiscount;

    Double price;

    public SearchProductReponse(Integer id, String name, String brand, String mainImageUrl, Double priceBeforeDiscount, Double price, Double Rating) {
        this.id = id;
        this.name = name;
        this.brand = brand;
        this.mainImageUrl = mainImageUrl;
        this.priceBeforeDiscount = priceBeforeDiscount;
        this.price = price;
        this.rating = Rating;
    }
}

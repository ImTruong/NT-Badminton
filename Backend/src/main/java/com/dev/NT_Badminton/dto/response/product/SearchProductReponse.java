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

    Double price;

    Double priceAfterDiscount;

    public SearchProductReponse(Integer id, String name, String brand, String mainImageUrl, Double price, Double priceAfterDiscount, Double Rating) {
        this.id = id;
        this.name = name;
        this.brand = brand;
        this.mainImageUrl = mainImageUrl;
        this.price = price;
        this.priceAfterDiscount = priceAfterDiscount;
        this.rating = Rating;
    }
}

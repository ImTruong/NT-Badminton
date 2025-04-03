package com.dev.NT_Badminton.dto.response.product;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

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

    Integer optionId;

    Integer optionValueId;

}

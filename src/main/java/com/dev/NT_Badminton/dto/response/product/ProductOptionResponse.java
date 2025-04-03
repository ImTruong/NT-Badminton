package com.dev.NT_Badminton.dto.response.product;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.HashMap;
import java.util.Map;

@Data
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProductOptionResponse {

    Integer id;

    String name;

    Map<String, Integer> values = new HashMap<>();

    public ProductOptionResponse(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

}

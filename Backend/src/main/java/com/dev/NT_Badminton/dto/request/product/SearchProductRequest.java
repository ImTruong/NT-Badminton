package com.dev.NT_Badminton.dto.request.product;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SearchProductRequest {

    String name;

    String brand;

    List<Integer> categoryIds;

    Double minPrice;

    Double maxPrice;

    Integer rating;

}

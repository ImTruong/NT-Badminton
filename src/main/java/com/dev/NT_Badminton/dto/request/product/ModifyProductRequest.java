package com.dev.NT_Badminton.dto.request.product;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ModifyProductRequest {

    Integer id;

    @NotBlank(message = "Product name is required")
    String name;

    @NotBlank(message = "Product description is required")
    String description;

    String shortDescription;

    @NotBlank(message = "Product brand is required")
    String brand;

    @NotNull(message = "Category id is required")
    Integer categoryId;

    Integer mainImageId;

    Integer coverImageId;

    List<Integer> imageIds;

    String type;
}

package com.dev.NT_Badminton.dto.request.product;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ModifyProductOptionRequest {

    Integer id;

    @NotBlank(message = "Product option name is required")
    String name;

    String description;

    @NotNull(message = "Product id is required")
    Integer productId;

    String type;

}

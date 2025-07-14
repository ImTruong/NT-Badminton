package com.dev.NT_Badminton.dto.request.product;

import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ModifyProductOptionValueRequest {

    Integer id;

    @NotNull(message = "Product option id is required")
    Integer productOptionId;

    @NotNull(message = "Product option value is required")
    String value;

    String type;

}

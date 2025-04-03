package com.dev.NT_Badminton.dto.request.product;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ModifyProductVariantRequest {

    Integer id;

    @NotNull(message = "Product option value ids are required")
    @Size(min = 1, message = "At least one product option value id is required")
    List<Integer> productOptionValueIds;

    @NotNull(message = "Product id is required")
    Integer productId;

    String sku;

    @NotNull(message = "Quantity is required")
    Integer quantity;

    @NotNull(message = "Price is required")
    Double price;

    String type;

}

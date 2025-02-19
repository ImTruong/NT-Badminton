package com.dev.NT_Badminton.dto.request.product;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class ProductOptionRequest {
    @NotNull
    private String name;

    private String description;

    @NotNull
    private String productId;

    List<String> values;
}

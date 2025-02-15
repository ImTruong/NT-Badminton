package com.dev.NT_Badminton.dto.request.product;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateProductRequest extends CreateProductRequest {
    @NotBlank(message = "Id can't not be blank")
    private int id;
}

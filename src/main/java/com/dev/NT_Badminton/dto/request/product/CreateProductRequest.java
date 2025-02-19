package com.dev.NT_Badminton.dto.request.product;

import com.dev.NT_Badminton.dto.constant.ActiveStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreateProductRequest {
    @NotBlank
    String name;

    @NotBlank
    String description;

    @NotBlank
    String shortDescription;

    @NotNull
    ActiveStatus status;

    int categoryId;

    int mainImageId;

    int coverImageId;

    @NotNull
    @Size(min = 1)
    List<Integer> imageIds;

    List<ProductOptionRequest> optionRequests;
}

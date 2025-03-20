package com.dev.NT_Badminton.dto.request.rating;

import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreateRatingRequest {

    @NotNull(message = "Product id can't not be null")
    Integer productId;

    @NotNull(message = "Rate can't not be null")
    Integer rate;

    String description;
}

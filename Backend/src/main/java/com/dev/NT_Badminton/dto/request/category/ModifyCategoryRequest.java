package com.dev.NT_Badminton.dto.request.category;

import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ModifyCategoryRequest {

    Integer id;

    @NotBlank(message = "Name is required")
    String name;

    String shortDescription;

    Integer parentId;

    Integer imageId;

    String type;

}

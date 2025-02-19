package com.dev.NT_Badminton.dto.request.category;

import com.dev.NT_Badminton.dto.constant.ActiveStatus;
import com.dev.NT_Badminton.entities.categories.constant.CategoryType;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreateCategoryReq {
    @NotNull
    String name;

    String shortDescription;

    int parent_id;

    ActiveStatus status;
    @NotNull
    CategoryType type;

    Integer imageId;
}

package com.dev.NT_Badminton.dto.request.blog;

import com.dev.NT_Badminton.dto.constant.ActiveStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateBlogRequest {
    @NotBlank
    private String title;

    @NotNull
    private String shortDescription;

    @NotNull
    private String description;

    private int imageId;

    private int categoryId;

    private ActiveStatus status;
}

package com.dev.NT_Badminton.dto.request.blog;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UpdateBlogRequest extends CreateBlogRequest{
    @NotBlank(message = "Id can't not be blank")
    private int id;
}

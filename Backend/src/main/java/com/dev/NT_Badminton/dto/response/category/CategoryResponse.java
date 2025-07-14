package com.dev.NT_Badminton.dto.response.category;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CategoryResponse {

    Integer categoryId;

    String name;

    String slug;

    String shortDescription;

    List<CategoryResponse> children;

    String imageUrl;

    public CategoryResponse(Integer categoryId, String name, String slug, String shortDescription, String imageUrl) {
        this.categoryId = categoryId;
        this.name = name;
        this.slug = slug;
        this.shortDescription = shortDescription;
        this.imageUrl = imageUrl;
    }
}

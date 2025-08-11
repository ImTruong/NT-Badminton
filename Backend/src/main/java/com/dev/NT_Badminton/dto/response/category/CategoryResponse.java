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

    public CategoryResponse(Integer categoryId, String name, String slug, String shortDescription) {
        this.categoryId = categoryId;
        this.name = name;
        this.slug = slug;
        this.shortDescription = shortDescription;
    }
}

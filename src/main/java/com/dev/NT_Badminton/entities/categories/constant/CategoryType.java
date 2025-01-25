package com.dev.NT_Badminton.entities.categories.constant;

import com.dev.NT_Badminton.dto.constant.BaseEnum;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum CategoryType implements BaseEnum<Integer> {
    PRODUCT,
    BLOG;

    @JsonCreator
    public static CategoryType fromValue(int value) {
        for (CategoryType categoryType : values()) {
            if (categoryType.toValue() == value) {
                return categoryType;
            }
        }
        return null;
    }

    @JsonValue
    public Integer toValue() {
        return ordinal();
    }
}
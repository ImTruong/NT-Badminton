package com.dev.NT_Badminton.entities.products.constant;

import com.dev.NT_Badminton.dto.constant.BaseEnum;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum ProductImageType implements BaseEnum<Integer> {
    COVER,
    MAIN,
    OTHER;

    @JsonCreator
    public static ProductImageType fromValue(int value) {
        for (ProductImageType productImageType : values()) {
            if (productImageType.toValue() == value) {
                return productImageType;
            }
        }
        return null;
    }

    @JsonValue
    public Integer toValue() {
        return ordinal();
    }
}


package com.dev.NT_Badminton.dto.constant;

import com.fasterxml.jackson.annotation.JsonValue;

public interface BaseEnum<T> {
    @JsonValue
    T toValue();
}

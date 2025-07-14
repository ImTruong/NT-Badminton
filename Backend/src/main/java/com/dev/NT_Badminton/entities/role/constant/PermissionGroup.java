package com.dev.NT_Badminton.entities.role.constant;

import com.dev.NT_Badminton.dto.constant.BaseEnum;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum PermissionGroup implements BaseEnum<String> {
    STATISTIC,
    ACCOUNT;

    @JsonCreator
    public static PermissionGroup fromValue(String value) {
        for (PermissionGroup column : values()) {
            if (column.toValue().equals(value)) {
                return column;
            }
        }
        return null;
    }

    @Override
    @JsonValue
    public String toValue() {
        return name();
    }
}

package com.dev.NT_Badminton.entities.role.constant;


import com.dev.NT_Badminton.dto.constant.BaseEnum;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum PermissionType implements BaseEnum<String> {
    ADMIN_MANAGER,
    ADMIN_BLOG,
    ADMIN_ACCOUNT,
    USER;

    @JsonCreator
    public static PermissionType fromValue(String value) {
        for (PermissionType column : values()) {
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

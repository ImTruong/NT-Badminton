package com.dev.NT_Badminton.entities.role.constant;


import com.dev.NT_Badminton.dto.constant.BaseEnum;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum PermissionType implements BaseEnum<String> {
    ADMIN(1),
    USER(2),
    ADMIN_ACCOUNT(3),
    ADMIN_BLOG(4),
    ADMIN_MANAGER(5);

    private final int roleId;  // Lưu giá trị số

    PermissionType(int roleId) {
        this.roleId = roleId;
    }

    public int getRoleId() {
        return roleId;
    }

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

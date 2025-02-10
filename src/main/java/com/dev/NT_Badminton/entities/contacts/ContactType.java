package com.dev.NT_Badminton.entities.contacts;

import com.dev.NT_Badminton.dto.constant.BaseEnum;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum ContactType implements BaseEnum<String> {
    MAIN(1),
    SUB(2);

    private final int typeId;

    ContactType(int typeId) {
        this.typeId = typeId;
    }

    public int getTypeId() {
        return typeId;
    }

    @JsonCreator
    public static ContactType fromValue(String value) {
        for (ContactType column : values()) {
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

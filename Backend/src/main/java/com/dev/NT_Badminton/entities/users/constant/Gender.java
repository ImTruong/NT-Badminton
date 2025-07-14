package com.dev.NT_Badminton.entities.users.constant;

import com.dev.NT_Badminton.dto.constant.BaseEnum;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum Gender implements BaseEnum<Integer> {
    MALE,
    FEMALE,
    OTHER;

    @JsonCreator
    public static Gender fromValue(int value){
        for (Gender column: values()){
            if(column.toValue()==value)
                return column;
        }
        return null;
    }

    @JsonValue
    public Integer toValue() {
        return ordinal();
    }
}
package com.dev.NT_Badminton.entities.payments.constant;

import com.dev.NT_Badminton.dto.constant.BaseEnum;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum PaymentMethod implements BaseEnum<Integer> {
    COD,
    VNPAY;

    @JsonCreator
    public static PaymentMethod fromValue(Integer value) {
        for (PaymentMethod method : PaymentMethod.values()) {
            if(method.toValue().equals(value))
                return method;
        }
        return null;
    }

    @JsonValue
    public Integer toValue() {
        return ordinal();
    }
}
package com.dev.NT_Badminton.entities.payments.constant;

import com.dev.NT_Badminton.dto.constant.BaseEnum;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum PaymentStatus implements BaseEnum<Integer> {
    PAID,
    UNPAID;

    @JsonCreator
    public static PaymentStatus fromValue(Integer value) {
        for (PaymentStatus status : PaymentStatus.values()) {
            if(status.toValue().equals(value))
                return status;
        }
        return null;
    }

    @JsonValue
    public Integer toValue() {
        return ordinal();
    }
}

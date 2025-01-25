package com.dev.NT_Badminton.entities.orders.constant;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum DeliveryStatus {
    PENDING,
    SHIPPED,
    DELIVERED,
    CANCELLED;

    @JsonCreator
    public static DeliveryStatus fromValue(Integer value) {
        for (DeliveryStatus status : DeliveryStatus.values()) {
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

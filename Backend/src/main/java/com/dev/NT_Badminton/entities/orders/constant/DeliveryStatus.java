package com.dev.NT_Badminton.entities.orders.constant;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import jakarta.persistence.EntityNotFoundException;

public enum DeliveryStatus {
    PENDING,
    DELIVERING,
    SHIPPED,
    CANCELLED;

    @JsonCreator
    public static DeliveryStatus fromValue(Integer value) {
        for (DeliveryStatus status : DeliveryStatus.values()) {
            if(status.toValue().equals(value))
                return status;
        }
        throw new EntityNotFoundException("Delivery status not found with value: " + value);
    }

    @JsonValue
    public Integer toValue() {
        return ordinal();
    }
}

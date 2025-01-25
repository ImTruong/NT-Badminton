package com.dev.NT_Badminton.entities.payments;

import com.dev.NT_Badminton.entities.BaseEntity;
import com.dev.NT_Badminton.entities.payments.constant.PaymentMethod;
import com.dev.NT_Badminton.entities.payments.constant.PaymentStatus;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "payments")
public class Payment extends BaseEntity {
    PaymentMethod method;

    PaymentStatus status;
}

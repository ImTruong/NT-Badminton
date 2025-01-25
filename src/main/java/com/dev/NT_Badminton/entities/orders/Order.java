package com.dev.NT_Badminton.entities.orders;

import com.dev.NT_Badminton.entities.BaseEntity;
import com.dev.NT_Badminton.entities.orders.constant.DeliveryStatus;
import jakarta.persistence.Column;
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
@Table(name = "orders")
public class Order extends BaseEntity {
    Integer userId;

    Integer paymentId;

    Integer contactId;

    @Column(name = "delivery_status", columnDefinition = "tinyint")
    DeliveryStatus deliveryStatus;
}

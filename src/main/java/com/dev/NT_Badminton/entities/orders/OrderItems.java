package com.dev.NT_Badminton.entities.orders;

import com.dev.NT_Badminton.entities.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "order_items")
public class OrderItems extends BaseEntity {
    Integer orderId;

    @Column(name = "product_variant_id")
    Integer productVariantId;

    int quantity;
}

package com.dev.NT_Badminton.entities.carts;

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
@Builder
@Table(name = "carts")
public class Cart extends BaseEntity {

    @Column(name = "product_variant_id")
    Integer productVariantId;

    Integer userId;

    int quantity;

}

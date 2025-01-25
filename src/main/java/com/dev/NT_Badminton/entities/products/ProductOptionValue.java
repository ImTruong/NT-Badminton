package com.dev.NT_Badminton.entities.products;

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
@Table(name = "product_option_values")
public class ProductOptionValue extends BaseEntity {
    @Column(name = "product_option_id", columnDefinition = "int")
    Integer product_option_id;

    String value;

    Integer additionalPrice;
}

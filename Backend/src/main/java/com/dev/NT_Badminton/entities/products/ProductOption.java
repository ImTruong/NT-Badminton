package com.dev.NT_Badminton.entities.products;

import com.dev.NT_Badminton.entities.BaseEntity;
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
@Table(name = "product_options")
public class ProductOption extends BaseEntity {
    String name;

    String description;

    Integer productId;
}

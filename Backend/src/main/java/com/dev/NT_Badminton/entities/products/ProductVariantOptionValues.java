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
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "product_variant_option_values")
public class ProductVariantOptionValues extends BaseEntity {

    int productVariantId;

    int productOptionValueId;

}

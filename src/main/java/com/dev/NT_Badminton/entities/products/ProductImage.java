package com.dev.NT_Badminton.entities.products;

import com.dev.NT_Badminton.entities.BaseEntity;
import com.dev.NT_Badminton.entities.products.constant.ProductImageType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Builder
@Table(name = "product_images")
public class ProductImage extends BaseEntity {

    Integer productId;

    Integer imageId;

    @Column(name = "type", columnDefinition = "tinyint")
    ProductImageType type;
}

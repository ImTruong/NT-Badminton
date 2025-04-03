package com.dev.NT_Badminton.entities.products;

import com.dev.NT_Badminton.dto.constant.ActiveStatus;
import com.dev.NT_Badminton.entities.BaseEntity;
import com.dev.NT_Badminton.entities.upload_file.UploadFile;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "products")
public class Product extends BaseEntity {
    String name;

    String slug;

    String brand;

    @Column(name = "short_description")
    String shortDescription;

    String description;

    @Column(name = "category_id", columnDefinition = "int")
    Integer categoryId;

}

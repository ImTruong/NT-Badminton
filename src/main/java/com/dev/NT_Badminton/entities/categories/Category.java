package com.dev.NT_Badminton.entities.categories;


import com.dev.NT_Badminton.dto.constant.ActiveStatus;
import com.dev.NT_Badminton.entities.BaseEntity;
import com.dev.NT_Badminton.entities.categories.constant.CategoryType;
import com.dev.NT_Badminton.entities.upload_file.UploadFile;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "categories")
public class Category extends BaseEntity {
    String name;
    String slug;
    String shortDescription;

    @Column(name = "status", columnDefinition = "tinyint")
    ActiveStatus status;

    @Column(name = "type", columnDefinition = "tinyint")
    CategoryType type;

    Integer imageId;

    @Transient
    UploadFile image;
}

package com.dev.NT_Badminton.entities.blogs;

import com.dev.NT_Badminton.dto.constant.ActiveStatus;
import com.dev.NT_Badminton.entities.BaseEntity;
import com.dev.NT_Badminton.entities.categories.Category;
import com.dev.NT_Badminton.entities.upload_file.UploadFile;
import com.dev.NT_Badminton.entities.users.AppUser;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "blogs")

public class Blog extends BaseEntity {
    String title;

    String slug;

    @Column(name = "short_description", columnDefinition = "text")
    String shortDescription;

    @Column(name = "description", columnDefinition = "text")
    String description;

    Integer imageId;

    Integer categoryId;

    Integer userId;

    ActiveStatus status;

    @Temporal(TemporalType.TIMESTAMP)
    Date publishDate;

    @Transient
    UploadFile image;

    @Transient
    AppUser author;

    @Transient
    Category category;
}

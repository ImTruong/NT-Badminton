package com.dev.NT_Badminton.entities.upload_file;

import com.dev.NT_Badminton.entities.BaseEntity;
import com.dev.NT_Badminton.entities.upload_file.constant.UploadFileType;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Table(name = "upload_files")
@Entity
public class UploadFile extends BaseEntity {

    String originUrl;

    String thumbUrl;

    @Column(columnDefinition = "tinyint")
    UploadFileType type;

    Integer width;

    Integer height;

    Integer size;

    @Column(name = "public_id")
    String publicId;

}

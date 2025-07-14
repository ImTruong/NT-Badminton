package com.dev.NT_Badminton.entities.staffs;

import com.dev.NT_Badminton.entities.BaseEntity;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Table(name = "staffs")
@Entity
public class Staff extends BaseEntity {
    Integer branchId;

    Integer userId;
}

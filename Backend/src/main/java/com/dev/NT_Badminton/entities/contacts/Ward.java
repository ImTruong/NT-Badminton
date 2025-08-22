package com.dev.NT_Badminton.entities.contacts;

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
@Table(name = "wards")
public class Ward extends BaseEntity {

    String name;

    Integer districtId;

}

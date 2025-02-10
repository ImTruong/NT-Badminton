package com.dev.NT_Badminton.entities.role;

import com.dev.NT_Badminton.dto.constant.ActiveStatus;
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
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "roles")
public class Role extends BaseEntity {

    String name;

    String note;

    @Column(name = "type", columnDefinition = "tinyint")
    int type;

    @Column(name = "status", columnDefinition = "tinyint")
    ActiveStatus status;

}

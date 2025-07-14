package com.dev.NT_Badminton.entities.contacts;

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
@Table(name = "contacts")
public class Contact extends BaseEntity {
    String firstName;

    String lastName;

    String phone;

    String email;

    Integer city;

    Integer district;

    String streetAddress;

    String note;

    @Column(name = "type", columnDefinition = "tinyint")
    int type;

    @Column(name = "user_id", columnDefinition = "int")
    Integer userId;
}

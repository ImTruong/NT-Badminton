package com.dev.NT_Badminton.entities.users;

import com.dev.NT_Badminton.dto.constant.ActiveStatus;
import com.dev.NT_Badminton.entities.BaseEntity;
import com.dev.NT_Badminton.entities.users.constant.Gender;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Table(name = "users")
@Entity
public class User extends BaseEntity {
    String code;

    String phone;

    String email;

    String name;

    String address;

    String password;

    Date birthday;

    @Column(name = "gender", columnDefinition = "int")
    Gender gender;

    Integer roleId;

    Integer userId;

    Integer avatarId;

    @Column(name = "status", columnDefinition = "INT")
    ActiveStatus status;
}

package com.dev.NT_Badminton.entities.users;

import com.dev.NT_Badminton.dto.constant.ActiveStatus;
import com.dev.NT_Badminton.entities.BaseEntity;
import com.dev.NT_Badminton.entities.role.Role;
import com.dev.NT_Badminton.entities.users.constant.Gender;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
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
public class AppUser extends BaseEntity {
    String code;

    String phone;

    String email;

    String name;

    String address;

    String password;

    Date birthday;

    @Column(name = "gender", columnDefinition = "int")
    Gender gender;

    @ManyToOne(fetch = FetchType.EAGER) // Lấy role ngay khi load user
    @JoinColumn(name = "roleId", referencedColumnName = "id", insertable = false, updatable = false)
    Role role;

    Integer userId;

    Integer avatarId;

    @Column(name = "status", columnDefinition = "INT")
    ActiveStatus status;

    public String getRoleName() {
        return role != null ? role.getName() : "ROLE_USER"; // Tránh null, mặc định là ROLE_USER
    }
}

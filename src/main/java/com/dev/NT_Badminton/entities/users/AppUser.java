package com.dev.NT_Badminton.entities.users;

import com.dev.NT_Badminton.dto.constant.ActiveStatus;
import com.dev.NT_Badminton.entities.BaseEntity;
import com.dev.NT_Badminton.entities.role.Role;
import com.dev.NT_Badminton.entities.upload_file.UploadFile;
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
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Table(name = "users")
@Entity
public class AppUser extends BaseEntity {
    String code;

    String email;

    String name;

    String password;

    Date birthday;

    @Column(name = "role_id")
    private Integer roleId;

    @Column(name = "gender", columnDefinition = "int")
    Gender gender;

    @ManyToOne(fetch = FetchType.EAGER) // Lấy role ngay khi load user
    @JoinColumn(name = "role_id", referencedColumnName = "id", insertable = false, updatable = false)
    Role role;

    @Column(name = "avatar_id")
    Integer avatarId;

    @Column(name = "status", columnDefinition = "INT")
    ActiveStatus status;

    @OneToOne
    @JoinColumn(name = "avatar_id", referencedColumnName = "id", insertable = false, updatable = false)
    private UploadFile avatar;

    public String getRoleName() {
        return role != null ? role.getName() : "ROLE_USER"; // Tránh null, mặc định là ROLE_USER
    }
}

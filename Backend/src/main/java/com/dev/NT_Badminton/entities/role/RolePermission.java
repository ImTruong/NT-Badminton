package com.dev.NT_Badminton.entities.role;

import com.dev.NT_Badminton.entities.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
@Table(name = "role_permission")
@Entity
public class RolePermission extends BaseEntity {

    @Column(name = "role_id")
    int roleId;

    @Column(name = "permission_id")
    int permissionId;

}

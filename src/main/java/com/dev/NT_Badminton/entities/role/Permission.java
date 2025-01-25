package com.dev.NT_Badminton.entities.role;

import com.dev.NT_Badminton.dto.constant.ActiveStatus;
import com.dev.NT_Badminton.entities.BaseEntity;
import com.dev.NT_Badminton.entities.role.constant.PermissionGroup;
import com.dev.NT_Badminton.entities.role.constant.PermissionType;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "permissions")
@Entity
public class Permission extends BaseEntity {
    String title;
    @Enumerated(value = EnumType.STRING)
    PermissionType permission;
    @Column(name = "parent_permission")
    @Enumerated(value = EnumType.STRING)
    PermissionGroup parentPermission;
    @Column(name = "is_view")
    boolean isView;
    @Column(name = "is_write")
    boolean isWrite;
    @Column(name = "is_approval")
    boolean isApproval;
    @Column(name = "is_decision")
    boolean isDecision;

    @Column(name = "type", columnDefinition = "tinyint")
    int type;
    @Column(name="status", columnDefinition = "tinyint")
    ActiveStatus status;
}

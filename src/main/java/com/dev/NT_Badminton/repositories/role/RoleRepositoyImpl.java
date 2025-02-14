package com.dev.NT_Badminton.repositories.role;

import com.dev.NT_Badminton.entities.role.QRole;
import com.dev.NT_Badminton.entities.role.Role;
import com.dev.NT_Badminton.repositories.BaseRepository;
import jakarta.persistence.EntityNotFoundException;

public class RoleRepositoyImpl extends BaseRepository implements RoleRepositoryCustom {

    @Override
    public Role getRoleById(int roleId) {
        QRole qRole = QRole.role;
        Role role = query().selectFrom(qRole)
                .where(qRole.id.eq(roleId))
                .fetchFirst();

        if (role == null) {
            throw new EntityNotFoundException("Role not found for ID: " + roleId);
        }

        return role;
    }

}

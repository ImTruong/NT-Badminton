package com.dev.NT_Badminton.repositories.role;

import com.dev.NT_Badminton.entities.role.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer>,RoleRepositoryCustom {
}

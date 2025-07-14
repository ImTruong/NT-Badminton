package com.dev.NT_Badminton.services.role;


import com.dev.NT_Badminton.entities.role.Role;
import com.dev.NT_Badminton.repositories.role.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public Role getRoleById(int roleId) {
        return roleRepository.getRoleById(roleId);
    }

}

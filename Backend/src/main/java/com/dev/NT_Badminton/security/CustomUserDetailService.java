package com.dev.NT_Badminton.security;

import com.dev.NT_Badminton.entities.role.Role;
import com.dev.NT_Badminton.entities.role.constant.PermissionType;
import com.dev.NT_Badminton.entities.users.AppUser;
import com.dev.NT_Badminton.repositories.user.UserRepository;
import com.dev.NT_Badminton.services.role.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
public class CustomUserDetailService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleService roleService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Optional<AppUser> optionalUserEntity = userRepository.findByEmailAndDeleted(username, false);
        AppUser userEntity = optionalUserEntity
                .orElseThrow(() -> new UsernameNotFoundException("AppUser doesn't exist"));
        userEntity.setRole(roleService.getRoleById(userEntity.getRoleId()));
        return new CustomUserDetails(userEntity);
    }
}
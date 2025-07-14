package com.dev.NT_Badminton.security;

import com.dev.NT_Badminton.entities.users.AppUser;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;

public class CustomUserDetails extends User {
    private final AppUser appUser;

    public CustomUserDetails(AppUser appUser) {
        super(appUser.getEmail(), appUser.getPassword(), getAuthorities(appUser));
        this.appUser = appUser;
    }

    private static Collection<? extends GrantedAuthority> getAuthorities(AppUser appUser) {
        return Collections.singletonList(new SimpleGrantedAuthority(appUser.getRoleName()));
    }

    public AppUser getAppUser() {
        return appUser;
    }

}

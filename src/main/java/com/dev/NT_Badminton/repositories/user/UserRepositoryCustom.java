package com.dev.NT_Badminton.repositories.user;

import com.dev.NT_Badminton.entities.users.AppUser;
import com.dev.NT_Badminton.repositories.BaseRepository;

import java.util.Optional;

public interface UserRepositoryCustom {
    Optional<AppUser> findByEmailAndDeleted(String email, boolean deleted);
    boolean existsByEmailAndDeleted(String email, boolean deleted);
}

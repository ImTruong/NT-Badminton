package com.dev.NT_Badminton.repositories.user;

import com.dev.NT_Badminton.entities.users.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<AppUser, Integer>, UserRepositoryCustom {
    Optional<AppUser> findByEmailAndDeleted(String email, boolean deleted);
}

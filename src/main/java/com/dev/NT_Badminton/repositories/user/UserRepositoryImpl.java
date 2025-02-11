package com.dev.NT_Badminton.repositories.user;

import com.dev.NT_Badminton.entities.users.AppUser;
import com.dev.NT_Badminton.entities.users.QAppUser;
import com.dev.NT_Badminton.repositories.BaseRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

public class UserRepositoryImpl extends BaseRepository implements UserRepositoryCustom{

    @Override
    public Optional<AppUser> findByEmailAndDeleted(String email, boolean deleted) {
        QAppUser qUser = QAppUser.appUser;

        AppUser user = query()
                .selectFrom(qUser)
                .where(
                        qUser.email.eq(email),
                        qUser.deleted.eq(deleted)
                )
                .fetchOne();

        return Optional.ofNullable(user);
    }

    @Override
    public boolean existsByEmailAndDeleted(String email, boolean deleted) {
        QAppUser qUser = QAppUser.appUser;

        Integer count = query()
                .selectOne()
                .from(qUser)
                .where(
                        qUser.email.eq(email),
                        qUser.deleted.eq(deleted)
                )
                .fetchFirst();

        return count != null;
    }
}

package com.dev.NT_Badminton.repositories;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class BaseRepository {

    @Autowired
    private HttpServletRequest httpServletRequest;

    @PersistenceContext
    private EntityManager entityManager;


    protected void flush() {
        entityManager.flush();
    }

    protected void detach(Object obj) {
        entityManager.detach(obj);
    }

    protected JPAQueryFactory query() {
        return new JPAQueryFactory(entityManager);
    }

    protected EntityManager getEntityManager() {
        return entityManager;
    }

}

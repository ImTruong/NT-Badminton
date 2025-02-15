package com.dev.NT_Badminton.repositories.blog;

import com.dev.NT_Badminton.entities.blogs.Blog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BlogRepository extends JpaRepository<Blog, Integer>,BlogRepositoryCustom {
    List<Blog> findAllByCategoryIdAndDeleted(int categoryId, boolean deleted);
}

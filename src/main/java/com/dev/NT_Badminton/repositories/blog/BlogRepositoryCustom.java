package com.dev.NT_Badminton.repositories.blog;

import com.dev.NT_Badminton.dto.constant.ActiveStatus;
import com.dev.NT_Badminton.entities.blogs.Blog;

import java.util.Date;
import java.util.List;

public interface BlogRepositoryCustom {
    List<Blog> getBlogs(int page, String name, Integer categoryId, ActiveStatus status, Boolean deleted) throws Exception;

    long countBlogs(String name, Integer categoryId, ActiveStatus status, Boolean deleted) throws Exception;

    Blog getDetailBlog(String slug);
}

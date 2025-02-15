package com.dev.NT_Badminton.services.blog;

import com.dev.NT_Badminton.dto.constant.ActiveStatus;
import com.dev.NT_Badminton.dto.request.IdsRequest;
import com.dev.NT_Badminton.dto.request.blog.CreateBlogRequest;
import com.dev.NT_Badminton.dto.request.blog.UpdateBlogRequest;
import com.dev.NT_Badminton.dto.response.ApiResponse;
import com.dev.NT_Badminton.entities.blogs.Blog;

import java.util.Date;
import java.util.List;

public interface BlogService {
    ApiResponse<List<Blog>> getBlogs(int page, String name, Integer categoryId, ActiveStatus status, Boolean deleted) throws Exception;

    Blog getDetailBlog(String slug, boolean isForAdmin) throws Exception;

    IdsRequest deleteBlogs(IdsRequest req) throws Exception;

    IdsRequest restoreBlogs(IdsRequest req) throws Exception;

    Blog createBlog(CreateBlogRequest req) throws Exception;

    Blog updateBlog(UpdateBlogRequest req) throws Exception;
}

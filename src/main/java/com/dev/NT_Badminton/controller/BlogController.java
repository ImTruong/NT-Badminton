package com.dev.NT_Badminton.controller;

import com.dev.NT_Badminton.dto.constant.ActiveStatus;
import com.dev.NT_Badminton.dto.request.IdsRequest;
import com.dev.NT_Badminton.dto.request.blog.CreateBlogRequest;
import com.dev.NT_Badminton.dto.request.blog.UpdateBlogRequest;
import com.dev.NT_Badminton.dto.response.ApiResponse;
import com.dev.NT_Badminton.entities.blogs.Blog;
import com.dev.NT_Badminton.services.blog.BlogService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/blog/")
@RequiredArgsConstructor
public class BlogController {
    private final BlogService blogService;

    @GetMapping("get-blogs")
    public ResponseEntity<ApiResponse<List<Blog>>> getBlogsForUser(@RequestParam int page,
                                                                   @RequestParam(required = false) String name,
                                                                   @RequestParam(required = false) int categoryId,
                                                                   @RequestParam(required = false)  ActiveStatus status,
                                                                   @RequestParam(required = false) Boolean deleted) throws Exception {
        return ResponseEntity.ok(blogService.getBlogs(page,name,categoryId,status,false));
    }

    @GetMapping("admin/get-blogs")
    public ResponseEntity<ApiResponse<List<Blog>>> getBlogsForAdmin(@RequestParam int page,
                                                                   @RequestParam(required = false) String name,
                                                                   @RequestParam(required = false) int categoryId,
                                                                   @RequestParam(required = false)  ActiveStatus status,
                                                                   @RequestParam(required = false) Boolean deleted) throws Exception {
        return ResponseEntity.ok(blogService.getBlogs(page,name,categoryId,status,deleted));
    }

    @GetMapping("get-detail-blog")
    public ResponseEntity<ApiResponse<Blog>> getDetailBlogForUser(@RequestParam String slug) throws Exception {
        return ResponseEntity.ok(new ApiResponse<>(true,"Get detail blog for user successfully!",blogService.getDetailBlog(slug,false)));
    }

    @GetMapping("admin/get-detail-blog")
    public ResponseEntity<ApiResponse<Blog>> getDetailBlogForAdmin(@RequestParam String slug) throws Exception {
        return ResponseEntity.ok(new ApiResponse<>(true,"Get detail blog for admin successfully!",blogService.getDetailBlog(slug,true)));
    }

    @PostMapping("admin/delete-blogs")
    public ResponseEntity<ApiResponse<IdsRequest>> deleteBlogs(@RequestBody IdsRequest req) throws Exception {
        return ResponseEntity.ok(new ApiResponse<>(true,"Blog has been deleted!", blogService.deleteBlogs(req)));
    }

    @PostMapping("admin/restore-blogs")
    public ResponseEntity<ApiResponse<IdsRequest>> restoreBlogs(@RequestBody IdsRequest req) throws Exception {
        return ResponseEntity.ok(new ApiResponse<>(true,"Blog has been restored!", blogService.restoreBlogs(req)));
    }

    @PostMapping("admin/create-blog")
    public ResponseEntity<ApiResponse<Blog>> createBlog(@RequestBody CreateBlogRequest req) throws Exception {
        return ResponseEntity.ok(new ApiResponse<>(true,"Blog has been created!", blogService.createBlog(req)));
    }

    @PostMapping("admin/update-blog")
    public ResponseEntity<ApiResponse<Blog>> updateBlog(@RequestBody UpdateBlogRequest req) throws Exception {
        return ResponseEntity.ok(new ApiResponse<>(true,"Blog has been updated!", blogService.updateBlog(req)));
    }

}

package com.dev.NT_Badminton.services.blog;

import com.dev.NT_Badminton.dto.constant.ActiveStatus;
import com.dev.NT_Badminton.dto.request.IdsRequest;
import com.dev.NT_Badminton.dto.request.blog.CreateBlogRequest;
import com.dev.NT_Badminton.dto.request.blog.UpdateBlogRequest;
import com.dev.NT_Badminton.dto.response.ApiResponse;
import com.dev.NT_Badminton.entities.blogs.Blog;
import com.dev.NT_Badminton.entities.users.AppUser;
import com.dev.NT_Badminton.repositories.blog.BlogRepository;
import com.dev.NT_Badminton.services.user.UserService;
import com.dev.NT_Badminton.util.Utils;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RequiredArgsConstructor
@Service
public class BlogServiceImpl implements BlogService{
    private final BlogRepository blogRepository;
    private final ModelMapper modelMapper;
    private final UserService userService;

    @Override
    public ApiResponse<List<Blog>> getBlogs(int page, String name, Integer categoryId, ActiveStatus status, Boolean deleted) throws Exception {
        List<Blog> blogList = blogRepository.getBlogs(page, name, categoryId, status, deleted);

        long count = blogRepository.countBlogs(name, categoryId, status, deleted);
        if (blogList == null || count == 0) {
            throw new Exception("Blog not found!");
        }

        return new ApiResponse<>(true,blogList,count);
    }

    @Override
    public Blog getDetailBlog(String slug, boolean isForAdmin) throws Exception {
        Blog blog = blogRepository.getDetailBlog(slug);
        if(blog == null || (!isForAdmin && blog.getStatus() == ActiveStatus.INACTIVE) || blog.isDeleted()) {
            throw new Exception("Blog not found!");
        }

        return blog;
    }

    @Override
    public IdsRequest deleteBlogs(IdsRequest req) throws Exception {
        return handleBlogs(req,true);
    }

    @Override
    public IdsRequest restoreBlogs(IdsRequest req) throws Exception {
        return handleBlogs(req,false);
    }

    @Override
    public Blog createBlog(CreateBlogRequest req) throws Exception {
        AppUser user = userService.getUserFromSecurityContext();

        if(user == null) {
            throw new Exception("User not found!");
        }

        Blog blog = new Blog();
        blog.setUserId(user.getId());
        blog.setTitle(req.getTitle().trim());
        blog.setShortDescription(req.getShortDescription().trim());
        blog.setDescription(req.getDescription().trim());
        blog.setStatus(req.getStatus());
        blog.setImageId(req.getImageId());
        blog.setCategoryId(req.getCategoryId());
        blog.setSlug(Utils.removeCharacterVn(req.getTitle().trim().toLowerCase()) + Utils.randomString(8));

        if(req.getStatus().equals(ActiveStatus.ACTIVE)) {
            blog.setPublishDate(new Date());
        }

        return blogRepository.save(blog);
    }

    @Override
    public Blog updateBlog(UpdateBlogRequest req) throws Exception {
        Blog blog = blogRepository.findById(req.getId())
                .orElseThrow(() -> new Exception("Blog not found!"));

        blog.setTitle(req.getTitle());
        blog.setDescription(req.getDescription());
        blog.setShortDescription(req.getShortDescription());
        blog.setImageId(req.getImageId());
        blog.setCategoryId(req.getCategoryId());

        if (!blog.getStatus().equals(ActiveStatus.ACTIVE) && req.getStatus().equals(ActiveStatus.ACTIVE)) {
            blog.setPublishDate(new Date());
        } else {
            blog.setPublishDate(null);
        }

        blog.setStatus(req.getStatus());
        return blogRepository.save(blog);
    }

    private IdsRequest handleBlogs(IdsRequest req, boolean deleted) throws Exception {
        List<Blog> blogs = new ArrayList<>();

        for (Integer id : req.getIds()) {
            Blog blog = blogRepository.findById(id)
                    .orElseThrow(() -> new Exception("Blog not found!"));
            blog.setDeleted(deleted);
            blogs.add(blog);
        }

        if (!blogs.isEmpty()) {
            blogRepository.saveAll(blogs);
        }

        return req;
    }
}

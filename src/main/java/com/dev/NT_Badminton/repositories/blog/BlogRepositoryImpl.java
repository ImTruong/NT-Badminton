package com.dev.NT_Badminton.repositories.blog;

import com.dev.NT_Badminton.dto.constant.ActiveStatus;
import com.dev.NT_Badminton.entities.blogs.Blog;
import com.dev.NT_Badminton.entities.blogs.QBlog;

import com.dev.NT_Badminton.entities.categories.Category;
import com.dev.NT_Badminton.entities.categories.QCategory;
import com.dev.NT_Badminton.entities.upload_file.QUploadFile;
import com.dev.NT_Badminton.entities.users.AppUser;
import com.dev.NT_Badminton.entities.users.QAppUser;
import com.dev.NT_Badminton.repositories.BaseRepository;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import org.apache.commons.lang3.StringUtils;
import java.util.List;
import java.util.stream.Collectors;

import static com.dev.NT_Badminton.util.Utils.PAGE_SIZE;

public class BlogRepositoryImpl extends BaseRepository implements BlogRepositoryCustom {
    private BooleanBuilder getBooleanBuilder(String name, Integer categoryId, ActiveStatus status, Boolean deleted) {
        BooleanBuilder builder = new BooleanBuilder();
        QBlog qBlog = QBlog.blog;

        if (StringUtils.isEmpty(name)) {
            builder.and(qBlog.title.contains(name.trim()));
        }

        if (categoryId != null) {
            builder.and(qBlog.categoryId.eq(categoryId));
        }

        if (status != null) {
            builder.and(qBlog.status.eq(status));
        }

        if (deleted != null) {
            builder.and(qBlog.deleted.eq(deleted));
        }

        return builder;
    }

    @Override
    public List<Blog> getBlogs(int page, String name, Integer categoryId, ActiveStatus status, Boolean deleted) throws Exception {
        QBlog qBlog = QBlog.blog;

        return query().from(qBlog)
                .select(qBlog)
                .where(getBooleanBuilder(name, categoryId, status, deleted))
                .limit(PAGE_SIZE)
                .offset(PAGE_SIZE)
                .fetch()
                .stream()
                .peek(blog -> {
                    getAuthorForBlog(blog);
                    getImageForBlog(blog);
                })
                .collect(Collectors.toList());
    }

    @Override
    public long countBlogs(String name, Integer categoryId, ActiveStatus status, Boolean deleted) throws Exception {
        QBlog qBlog = QBlog.blog;

        Long count = query().from(qBlog)
                .select(qBlog.id.count())
                .where(getBooleanBuilder(name, categoryId, status, deleted))
                .fetchOne();

        return count != null ? count : 0;
    }

    @Override
    public Blog getDetailBlog(String slug) {
        QBlog qBlog = QBlog.blog;

        Blog blog = query().from(qBlog)
                .where(qBlog.slug.eq(slug))
                .select(qBlog)
                .fetchOne();

        getAuthorForBlog(blog);
        getImageForBlog(blog);

        return blog;
    }

    private void getAuthorForBlog(Blog blog) {
        if (blog != null) {
            QAppUser qUser = QAppUser.appUser;

            blog.setAuthor(query().from(qUser)
                    .select(Projections.fields(AppUser.class, qUser.id, qUser.name, qUser.code, qUser.email))
                    .where(qUser.id.eq(blog.getUserId()).and(qUser.deleted.eq(false)))
                    .fetchOne());
        }
    }

    private void getImageForBlog(Blog blog) {
        if (blog != null) {
            QUploadFile qUploadFile = QUploadFile.uploadFile;

            blog.setImage(query().from(qUploadFile)
                    .select(qUploadFile)
                    .where(qUploadFile.id.eq(blog.getImageId()).and(qUploadFile.deleted.eq(false)))
                    .fetchOne());
        }
    }

    private void getCategoryForBlog(Blog blog) {
        if (blog != null) {
            QCategory qCategory = QCategory.category;

            blog.setCategory(query().from(qCategory)
                    .select(Projections.fields(Category.class, qCategory.id,qCategory.name,qCategory.slug))
                    .where(qCategory.id.eq(blog.getCategoryId()).and(qCategory.deleted.eq(false)))
                    .fetchOne());
        }
    }
}

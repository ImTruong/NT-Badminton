package com.dev.NT_Badminton.repositories.category;

import com.dev.NT_Badminton.dto.constant.ActiveStatus;
import com.dev.NT_Badminton.entities.categories.Category;
import com.dev.NT_Badminton.entities.categories.QCategory;
import com.dev.NT_Badminton.entities.categories.constant.CategoryType;
import com.dev.NT_Badminton.entities.upload_file.QUploadFile;
import com.dev.NT_Badminton.repositories.BaseRepository;
import com.querydsl.core.BooleanBuilder;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

public class CategoryRepositoryImpl extends BaseRepository implements CategoryRepositoryCustom {
    private BooleanBuilder getBooleanBuilder(String name, String slug, CategoryType type, ActiveStatus status, Boolean deleted) {
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        QCategory qCategory = QCategory.category;

        if (StringUtils.isNotEmpty(name)) {
            booleanBuilder.and(qCategory.name.contains(name));
        }

        if (StringUtils.isNotEmpty(slug)) {
            booleanBuilder.and(qCategory.slug.contains(slug));
        }

        if (type != null) {
            booleanBuilder.and(qCategory.type.eq(type));
        }

        if (status != null) {
            booleanBuilder.and(qCategory.status.eq(status));
        }

        booleanBuilder.and(qCategory.deleted.eq(deleted != null ? deleted : false));

        return booleanBuilder;
    }
    @Override
    public List<Category> getCategories(String name, String slug, CategoryType type, ActiveStatus status, Boolean deleted) {
        QCategory qCategory = QCategory.category;
        QUploadFile qUploadFile = QUploadFile.uploadFile;

        return query().from(qCategory)
                .select(qCategory)
                .where(getBooleanBuilder(name, slug, type, status, deleted))
                .orderBy(qCategory.id.desc())
                .fetch()
                .stream()
                .peek(category -> category.setImage(
                        query().from(qUploadFile)
                                .select(qUploadFile)
                                .where(qUploadFile.id.eq(category.getImageId()))
                                .fetchOne()
                ))
                .collect(Collectors.toList());
    }

    @Override
    public long countCategories(String name, String slug, CategoryType type, ActiveStatus status, Boolean deleted) {
        QCategory qCategory = QCategory.category;

        Long count = query().from(qCategory)
                .select(qCategory.id.count())
                .where(getBooleanBuilder(name, slug, type, status, deleted))
                .fetchOne();

        return count != null ? count : 0;
    }
}

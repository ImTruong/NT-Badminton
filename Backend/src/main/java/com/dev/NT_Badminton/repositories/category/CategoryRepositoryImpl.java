package com.dev.NT_Badminton.repositories.category;

import com.dev.NT_Badminton.dto.response.category.CategoryResponse;
import com.dev.NT_Badminton.entities.categories.QCategory;
import com.dev.NT_Badminton.entities.upload_file.QUploadFile;
import com.dev.NT_Badminton.repositories.BaseRepository;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;

import java.util.List;

public class CategoryRepositoryImpl extends BaseRepository implements CategoryRepositoryCustom {

    @Override
    public List<CategoryResponse> getAllCategories() {
        QCategory qCategory = QCategory.category;
        List<CategoryResponse> categories = query()
                .select(Projections.constructor(CategoryResponse.class,
                        qCategory.id,
                        qCategory.name,
                        qCategory.slug,
                        qCategory.shortDescription
                ))
                .from(qCategory)
                .where(qCategory.deleted.eq(false)
                        .and(qCategory.parentId.isNull()))
                .fetch();

        categories.forEach(category -> {
            List<CategoryResponse> children = query()
                    .select(Projections.constructor(CategoryResponse.class,
                            qCategory.id,
                            qCategory.name,
                            qCategory.slug,
                            qCategory.shortDescription
                    ))
                    .from(qCategory)
                    .where(qCategory.deleted.eq(false)
                            .and(qCategory.parentId.eq(category.getCategoryId())))
                    .fetch();

            category.setChildren(children);
        });
        return categories;
    }



}

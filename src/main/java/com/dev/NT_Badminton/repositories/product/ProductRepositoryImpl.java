package com.dev.NT_Badminton.repositories.product;

import com.dev.NT_Badminton.dto.constant.ActiveStatus;
import com.dev.NT_Badminton.entities.products.Product;
import com.dev.NT_Badminton.entities.products.QProduct;
import com.dev.NT_Badminton.repositories.BaseRepository;
import com.querydsl.core.BooleanBuilder;
import io.micrometer.common.util.StringUtils;

import java.util.List;

import static com.dev.NT_Badminton.util.Utils.PAGE_SIZE;

public class ProductRepositoryImpl extends BaseRepository implements ProductRepositoryCustom {

    private BooleanBuilder getBooleanBuilder(String name, Integer categoryId, Boolean deleted, ActiveStatus status, Integer startPriceRange, Integer endPriceRange, String relatedProductSlug) {
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        QProduct qProduct = QProduct.product;

        if(StringUtils.isNotEmpty(name)) {
            booleanBuilder.and(qProduct.name.contains(name.trim()));
        }

        if(categoryId != null)
            booleanBuilder.and(qProduct.categoryId.eq(categoryId));

        if(status != null)
            booleanBuilder.and(qProduct.status.eq(status));

        if(deleted != null)
            booleanBuilder.and(qProduct.deleted.eq(deleted));

        if(startPriceRange != null && endPriceRange != null)
            booleanBuilder.and(qProduct.original_price.between(startPriceRange, endPriceRange));

        if(StringUtils.isNotEmpty(relatedProductSlug))
            booleanBuilder.and(qProduct.slug.notEqualsIgnoreCase(relatedProductSlug));

        return booleanBuilder;
    }

    @Override
    public List<Product> getProducts(int page, String name, Integer categoryId, Boolean deleted, ActiveStatus status, Integer startPriceRange, Integer endPriceRange, String relatedProductSlug) {
        QProduct qProduct = QProduct.product;
        return query().from(qProduct)
                .select(qProduct)
                .where(getBooleanBuilder(name,categoryId,deleted,status,startPriceRange,endPriceRange,relatedProductSlug))
                .limit(PAGE_SIZE)
                .offset((long) page * PAGE_SIZE)
                .orderBy(qProduct.id.desc())
                .fetch();
    }

    @Override
    public long countProducts(String name, Integer categoryId, Boolean deleted, ActiveStatus status, Integer startPriceRange, Integer endPriceRange, String relatedProductSlug) {
        QProduct qProduct = QProduct.product;

        Long count = query().from(qProduct)
                .select(qProduct.id.count())
                .where(getBooleanBuilder(name, categoryId, deleted, status, startPriceRange, endPriceRange, relatedProductSlug))
                .fetchOne();

        return count != null ? count : 0;
    }
}

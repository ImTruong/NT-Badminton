package com.dev.NT_Badminton.repositories.product;

import com.dev.NT_Badminton.dto.constant.ActiveStatus;
import com.dev.NT_Badminton.entities.products.Product;
import com.dev.NT_Badminton.entities.products.QProduct;
import com.dev.NT_Badminton.entities.products.QProductVariants;
import com.dev.NT_Badminton.repositories.BaseRepository;
import com.querydsl.core.BooleanBuilder;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Optional;

import static com.dev.NT_Badminton.util.Utils.PAGE_SIZE;

public class ProductRepositoryImpl extends BaseRepository implements ProductRepositoryCustom {

    private BooleanBuilder getBooleanBuilder(String name, Integer categoryId, Boolean deleted, ActiveStatus status, Integer startPriceRange, Integer endPriceRange,String relatedProductSlug) {
        BooleanBuilder builder = new BooleanBuilder();
        QProduct qProduct = QProduct.product;

        if(StringUtils.isNotEmpty(name)) {
            builder.and(qProduct.name.eq(name));
        }

        if(categoryId != null) {
            builder.and(qProduct.categoryId.eq(categoryId));
        }

        if(status != null) {
            builder.and(qProduct.status.eq(status));
        }

        if(deleted != null) {
            builder.and(qProduct.deleted.eq(deleted));
        }
        if(startPriceRange != null && endPriceRange != null) {
            builder.and(getPriceForProduct(qProduct, startPriceRange, endPriceRange));
        }
        if(StringUtils.isNotEmpty(relatedProductSlug)) {
            builder.and(qProduct.slug.notEqualsIgnoreCase(relatedProductSlug));
        }

        return builder;
    }
    @Override
    public List<Product> getProducts(int page, String name, Integer categoryId, Boolean deleted, ActiveStatus status, Integer startPriceRange, Integer endPriceRange, String relatedProductSlug) {
        QProduct qProduct = QProduct.product;

        return query().from(qProduct)
                .select(qProduct)
                .where(getBooleanBuilder(name, categoryId, deleted, status, startPriceRange, endPriceRange, relatedProductSlug))
                .limit(PAGE_SIZE)
                .offset(page * PAGE_SIZE)
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

    public BooleanBuilder getPriceForProduct(QProduct product, Integer startPriceRange, Integer endPriceRange) {
        if (product == null) {
            return new BooleanBuilder();
        }

        QProductVariants qProductVariant = QProductVariants.productVariants;
        BooleanBuilder priceCondition = new BooleanBuilder();

        priceCondition.and(qProductVariant.productId.eq(product.id));

        if (startPriceRange != null) {
            priceCondition.and(qProductVariant.price.goe(startPriceRange)); // Giá >= startPriceRange
        }
        if (endPriceRange != null) {
            priceCondition.and(qProductVariant.price.loe(endPriceRange)); // Giá <= endPriceRange
        }

        return priceCondition;
    }


}

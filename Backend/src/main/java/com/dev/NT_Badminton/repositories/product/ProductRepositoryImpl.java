package com.dev.NT_Badminton.repositories.product;

import com.dev.NT_Badminton.dto.request.product.SearchProductRequest;
import com.dev.NT_Badminton.dto.response.product.*;
import com.dev.NT_Badminton.dto.response.rating.RatingResponse;
import com.dev.NT_Badminton.entities.categories.QCategory;
import com.dev.NT_Badminton.entities.discounts.QDiscount;
import com.dev.NT_Badminton.entities.products.*;
import com.dev.NT_Badminton.entities.products.constant.ProductImageType;
import com.dev.NT_Badminton.entities.rating.QRating;
import com.dev.NT_Badminton.entities.upload_file.QUploadFile;
import com.dev.NT_Badminton.entities.users.QAppUser;
import com.dev.NT_Badminton.repositories.BaseRepository;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQuery;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class ProductRepositoryImpl extends BaseRepository implements ProductRepositoryCustom {


    @Override
    public List<Integer> findProductIdsByOptionValueIds(List<Integer> productOptionValueIds, int productId) {
        QProductOptionValue qProductOptionValue = QProductOptionValue.productOptionValue;
        QProductOption qProductOption = QProductOption.productOption;

        Long productOptionCount = query()
                .select(qProductOption.countDistinct())
                .from(qProductOption)
                .where(qProductOption.productId.eq(productId))
                .fetchOne();

        if (productOptionCount == null || productOptionCount == 0) {
            return Collections.emptyList();
        }

        return query()
                .select(qProductOption.productId)
                .from(qProductOptionValue)
                .join(qProductOption).on(qProductOption.id.eq(qProductOptionValue.productOptionId))
                .where(qProductOptionValue.id.in(productOptionValueIds)
                        .and(qProductOption.productId.eq(productId)))
                .groupBy(qProductOption.productId)
                .having(qProductOptionValue.productOptionId.countDistinct().eq(productOptionCount))
                .fetch();
    }

    @Override
    public PageImpl<SearchProductReponse> findProducts(SearchProductRequest searchProductRequest, Pageable pageable) {
        QProduct qProduct = QProduct.product;
        QUploadFile qUploadFile = QUploadFile.uploadFile;
        QProductImage qProductImage = QProductImage.productImage;
        QProductVariants qProductVariants = QProductVariants.productVariants;
        QRating qRating = QRating.rating;
        QDiscount qDiscount = QDiscount.discount;

        BooleanBuilder predicate = new BooleanBuilder();

        Optional.ofNullable(searchProductRequest.getName())
                .filter(name -> !name.isBlank())
                .ifPresent(name -> predicate.and(qProduct.name.containsIgnoreCase(name)));

        Optional.ofNullable(searchProductRequest.getBrands())
                .filter(brands -> !brands.isEmpty())
                .ifPresent(brands -> predicate.and(qProduct.brand.in(brands)));

        Optional.ofNullable(searchProductRequest.getCategoryIds())
                .filter(categoryIds -> !categoryIds.isEmpty())
                .ifPresent(categoryIds -> predicate.and((findProductRootCategoryId(qProduct.id)).in(categoryIds)));

        Optional.ofNullable(searchProductRequest.getMinPrice())
                .ifPresent(minPrice -> predicate.and(qProductVariants.price.goe(minPrice)));

        Optional.ofNullable(searchProductRequest.getMaxPrice())
                .ifPresent(maxPrice -> predicate.and(qProductVariants.price.loe(maxPrice)));

        NumberExpression<Double> avgRating = qRating.rate.avg();
        NumberExpression<Double> minPrice = qProductVariants.price.min();
        NumberExpression<Double> maxDiscount = qDiscount.discountPercentages
                .max()
                .coalesce(0)
                .castToNum(Double.class)
                .divide(100);

        NumberExpression<Double> priceAfterDiscount = minPrice.multiply(Expressions.numberTemplate(Double.class, "1 - {0}", maxDiscount));

        JPAQuery<SearchProductReponse> query = query()
                .select(Projections.constructor(
                        SearchProductReponse.class,
                        qProduct.id,
                        qProduct.name,
                        qProduct.brand,
                        qUploadFile.originUrl,
                        minPrice.as("price"),
                        priceAfterDiscount.as("priceAfterDiscount"),
                        avgRating.as("rating")
                ))
                .from(qProduct)
                .leftJoin(qProductImage).on(qProduct.id.eq(qProductImage.productId))
                .leftJoin(qUploadFile).on(qProductImage.imageId.eq(qUploadFile.id))
                .leftJoin(qDiscount).on(qProduct.id.eq(qDiscount.productId)
                        .and(qDiscount.timeEnded.after(Timestamp.valueOf(LocalDateTime.now())))
                        .and(qDiscount.deleted.eq(false))
                        .and(qDiscount.timeStarted.before(Timestamp.valueOf(LocalDateTime.now())))
                )
                .join(qProductVariants).on(qProduct.id.eq(qProductVariants.productId))
                .leftJoin(qRating).on(qProduct.id.eq(qRating.productId))
                .where(
                        (qProductImage.type.eq(ProductImageType.MAIN).or(qProductImage.isNull()))
                                .and(qProduct.deleted.eq(false))
                                .and(qProductVariants.deleted.eq(false))
                                .and((qProductImage.deleted.eq(false)).or(qProductImage.isNull()))
                                .and(qUploadFile.deleted.eq(false).or(qUploadFile.isNull()))
                                .and(predicate)
                )
                .groupBy(qProduct.id, qUploadFile.originUrl);

        Optional.ofNullable(searchProductRequest.getRating())
                .ifPresent(minRating -> query.having(avgRating.goe(minRating)));
        JPAQuery<Long> countQuery = query()
                .select(qProduct.countDistinct())
                .from(qProduct)
                .join(qProductVariants).on(qProduct.id.eq(qProductVariants.productId))
                .where(predicate);

        List<SearchProductReponse> results = query
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = countQuery.fetchOne();

        return new PageImpl<>(results, pageable, total);
    }

    private JPQLQuery<Integer> findProductRootCategoryId(NumberExpression<Integer> productIdExpr) {
        QProduct qProductSub = new QProduct("p");
        QCategory qChildrenCategory = new QCategory("childCategory");
        QCategory qRootCategory = new QCategory("rootCategory");

        return JPAExpressions
                .select(
                        new CaseBuilder()
                                .when(qChildrenCategory.parentId.isNotNull())
                                .then(qRootCategory.id)
                                .otherwise(qChildrenCategory.id)
                )
                .from(qProductSub)
                .join(qChildrenCategory).on(qProductSub.categoryId.eq(qChildrenCategory.id))
                .leftJoin(qRootCategory).on(qChildrenCategory.parentId.eq(qRootCategory.id))
                .where(qProductSub.id.eq(productIdExpr));
    }

    @Override
    public ProductDetailResponse findProductDetailById(int productId) {
        QProduct qProduct = QProduct.product;
        QProductVariants qProductVariants = QProductVariants.productVariants;
        QProductImage qProductImage = QProductImage.productImage;
        QUploadFile qUploadFile = QUploadFile.uploadFile;
        QRating qRating = QRating.rating;
        QAppUser qAppUser = QAppUser.appUser;
        QProductOptionValue qProductOptionValue = QProductOptionValue.productOptionValue;
        QProductOption qProductOption = QProductOption.productOption;
        QDiscount qDiscount = QDiscount.discount;
        QProductVariantOptionValues qProductVariantOptionValues = QProductVariantOptionValues.productVariantOptionValues;
        ProductDetailResponse productDetailResponse = query()
                .select(Projections.constructor(
                        ProductDetailResponse.class,
                        qProduct.id,
                        qProduct.name,
                        qProduct.brand,
                        qProduct.shortDescription,
                        qProduct.description,
                        qProduct.categoryId
                ))
                .from(qProduct)
                .where(qProduct.id.eq(productId)
                        .and(qProduct.deleted.eq(false))
                )
                .fetchOne();
        if (productDetailResponse != null) {
            List<ProductImageResponse> images = query()
                    .select(Projections.constructor(
                            ProductImageResponse.class,
                            qProductImage.id,
                            qUploadFile.originUrl,
                            qProductImage.type
                    ))
                    .from(qProductImage)
                    .join(qUploadFile).on(qProductImage.imageId.eq(qUploadFile.id))
                    .where(qProductImage.productId.eq(productId)
                            .and(qProductImage.deleted.eq(false))
                            .and(qUploadFile.deleted.eq(false))
                    )
                    .fetch();
            List<RatingResponse> ratings = query()
                    .select(Projections.constructor(
                            RatingResponse.class,
                            qRating.id,
                            qRating.userId,
                            qAppUser.name,
                            qRating.rate,
                            qRating.description,
                            qRating.createdAt
                    ))
                    .from(qRating)
                    .join(qAppUser).on(qRating.userId.eq(qAppUser.id))
                    .where(qRating.productId.eq(productId)
                            .and(qRating.deleted.eq(false))
                    )
                    .fetch();


            List<Tuple> optionTuple = query()
                    .select(
                            qProductOption.id,
                            qProductOption.name,
                            qProductOptionValue.id,
                            qProductOptionValue.value
                    )
                    .from(qProductOption)
                    .join(qProductOptionValue).on(qProductOption.id.eq(qProductOptionValue.productOptionId))
                    .where(qProductOption.productId.eq(productId)
                            .and(qProductOption.deleted.eq(false))
                            .and(qProductOptionValue.deleted.eq(false))
                    )
                    .fetch();

            Map<Integer, ProductOptionResponse> optionMap = new HashMap<>();

            for (Tuple tuple : optionTuple) {
                Integer optionId = tuple.get(qProductOption.id);
                String optionName = tuple.get(qProductOption.name);
                Integer valueId = tuple.get(qProductOptionValue.id);
                String value = tuple.get(qProductOptionValue.value);

                optionMap.putIfAbsent(optionId, new ProductOptionResponse(optionId, optionName));

                optionMap.get(optionId).getValues().put(valueId,value);
            }

            List<ProductOptionResponse> options = new ArrayList<>(optionMap.values());

            NumberExpression<BigDecimal> maxDiscount = qDiscount.discountPercentages
                    .max()
                    .coalesce(0)
                    .castToNum(BigDecimal.class)
                    .divide(BigDecimal.valueOf(100));

            List<ProductVariantResponse> variants = query()
                    .select(Projections.constructor(
                            ProductVariantResponse.class,
                            qProductVariants.id,
                            qProductVariants.sku,
                            qProductVariants.price,
                            qProductVariants.price.multiply(
                                    Expressions.asNumber(
                                            qDiscount.discountPercentages.max().coalesce(0).divide(100)
                                    ).subtract(1).multiply(-1)
                            ).coalesce(qProductVariants.price),
                            qProductVariants.quantity
                    ))
                    .from(qProductVariants)
                    .leftJoin(qDiscount).on(qProductVariants.productId.eq(qDiscount.productId)
                            .and(qDiscount.timeEnded.after(Timestamp.valueOf(LocalDateTime.now())))
                            .and(qDiscount.deleted.eq(false))
                            .and(qDiscount.timeStarted.before(Timestamp.valueOf(LocalDateTime.now())))
                    )
                    .where(qProductVariants.productId.eq(productId)
                            .and(qProductVariants.deleted.eq(false))
                    )
                    .groupBy(qProductVariants.id, qProductVariants.sku, qProductVariants.price, qProductVariants.quantity)
                    .fetch();
            for (ProductVariantResponse variant : variants) {
                List<Tuple> variantOptionValues = query()
                        .select(
                                qProductOptionValue.productOptionId,
                                qProductVariantOptionValues.productOptionValueId
                        )
                        .from(qProductVariantOptionValues)
                        .join(qProductOptionValue).on(qProductVariantOptionValues.productOptionValueId.eq(qProductOptionValue.id))
                        .where(qProductVariantOptionValues.productVariantId.eq(variant.getId())
                                .and(qProductOptionValue.deleted.eq(false))
                        )
                        .fetch();
                variant.setOptionValues(variantOptionValues.stream()
                        .collect(Collectors.toMap(
                                t -> t.get(qProductOptionValue.productOptionId),
                                t -> t.get(qProductVariantOptionValues.productOptionValueId)
                        )));

            }
            productDetailResponse.setImages(images);
            productDetailResponse.setRatings(ratings);
            productDetailResponse.setOptions(options);
            productDetailResponse.setVariants(variants);
            return productDetailResponse;
        }
        return null;
    }

    @Override
    public List<String> getAllProductBrands() {
        QProduct qProduct = QProduct.product;
        return query().
                select(qProduct.brand)
                .from(qProduct)
                .where(qProduct.deleted.eq(false))
                .distinct()
                .fetch();
    }


}

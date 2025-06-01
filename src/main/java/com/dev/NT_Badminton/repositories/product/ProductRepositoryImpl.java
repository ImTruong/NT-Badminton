package com.dev.NT_Badminton.repositories.product;

import com.dev.NT_Badminton.dto.request.product.SearchProductRequest;
import com.dev.NT_Badminton.dto.response.product.*;
import com.dev.NT_Badminton.dto.response.rating.RatingResponse;
import com.dev.NT_Badminton.entities.discounts.QDiscount;
import com.dev.NT_Badminton.entities.products.*;
import com.dev.NT_Badminton.entities.products.constant.ProductImageType;
import com.dev.NT_Badminton.entities.rating.QRating;
import com.dev.NT_Badminton.entities.upload_file.QUploadFile;
import com.dev.NT_Badminton.repositories.BaseRepository;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.impl.JPAQuery;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;

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
    public List<SearchProductReponse> findProducts(SearchProductRequest searchProductRequest) {
        QProduct qProduct = QProduct.product;
        QUploadFile qUploadFile = QUploadFile.uploadFile;
        QProductImage qProductImage = QProductImage.productImage;
        QProductVariants qProductVariants = QProductVariants.productVariants;
        QRating qRating = QRating.rating;
        QDiscount qDiscount = QDiscount.discount;

        BooleanBuilder predicate = new BooleanBuilder();

        Optional.ofNullable(searchProductRequest.getName())
                .filter(name -> !name.isEmpty())
                .ifPresent(name -> predicate.and(qProduct.name.containsIgnoreCase(name)));

        Optional.ofNullable(searchProductRequest.getBrand())
                .filter(brand -> !brand.isEmpty())
                .ifPresent(brand -> predicate.and(qProduct.brand.containsIgnoreCase(brand)));

        Optional.ofNullable(searchProductRequest.getCategoryIds())
                .filter(categoryIds -> !categoryIds.isEmpty())
                .ifPresent(categoryIds -> predicate.and(qProduct.categoryId.in(categoryIds)));

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

        return query.fetch();
    }

    @Override
    public ProductDetailResponse findProductDetailById(int productId) {
        QProduct qProduct = QProduct.product;
        QProductVariants qProductVariants = QProductVariants.productVariants;
        QProductImage qProductImage = QProductImage.productImage;
        QUploadFile qUploadFile = QUploadFile.uploadFile;
        QRating qRating = QRating.rating;
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
                            qRating.rate,
                            qRating.description,
                            qRating.createdAt
                    ))
                    .from(qRating)
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

                optionMap.get(optionId).getValues().put(value, valueId);
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
                            qProductVariants.quantity,
                            qProductOptionValue.productOptionId,
                            qProductOptionValue.id
                    ))
                    .from(qProductVariants)
                    .leftJoin(qDiscount).on(qProductVariants.productId.eq(qDiscount.productId)
                            .and(qDiscount.timeEnded.after(Timestamp.valueOf(LocalDateTime.now())))
                            .and(qDiscount.deleted.eq(false))
                            .and(qDiscount.timeStarted.before(Timestamp.valueOf(LocalDateTime.now())))
                    )
                    .leftJoin(qProductVariantOptionValues).on(qProductVariants.id.eq(qProductVariantOptionValues.productVariantId))
                    .leftJoin(qProductOptionValue).on(qProductVariantOptionValues.productOptionValueId.eq(qProductOptionValue.id))
                    .where(qProductVariants.productId.eq(productId)
                            .and(qProductVariants.deleted.eq(false))
                    )
                    .groupBy(qProductVariants.id, qProductVariants.sku, qProductVariants.price, qProductVariants.quantity,
                            qProductOptionValue.productOptionId, qProductOptionValue.id)
                    .fetch();
            productDetailResponse.setImages(images);
            productDetailResponse.setRatings(ratings);
            productDetailResponse.setOptions(options);
            productDetailResponse.setVariants(variants);
            return productDetailResponse;
        }
        return null;
    }


}

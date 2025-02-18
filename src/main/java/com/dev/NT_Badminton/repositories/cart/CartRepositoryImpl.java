package com.dev.NT_Badminton.repositories.cart;


import com.dev.NT_Badminton.dto.response.cart.CartProductResponse;
import com.dev.NT_Badminton.entities.carts.QCart;
import com.dev.NT_Badminton.entities.discounts.QDiscount;
import com.dev.NT_Badminton.entities.products.*;
import com.dev.NT_Badminton.entities.products.constant.ProductImageType;
import com.dev.NT_Badminton.entities.upload_file.QUploadFile;
import com.dev.NT_Badminton.repositories.BaseRepository;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.Projections;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.stream.Collectors;

public class CartRepositoryImpl extends BaseRepository implements CartRepositoryCustom {

    @Override
    public PageImpl<CartProductResponse> getCartProductResponseByUserId(Integer userId, Pageable pageable) {
        QCart qCart = QCart.cart;
        QProductVariants qProductVariants = QProductVariants.productVariants;
        QDiscount qDiscount = QDiscount.discount;
        QProduct qProduct = QProduct.product;
        QProductImage qProductImage = QProductImage.productImage;
        QUploadFile qUploadFile = QUploadFile.uploadFile;
        QProductOptionValue qProductOptionValue = QProductOptionValue.productOptionValue;
        QProductOption qProductOption = QProductOption.productOption;
        QProductVariantOptionValues qProductVariantOptionValues = QProductVariantOptionValues.productVariantOptionValues;

        QueryResults<CartProductResponse> results = query()
                .select(Projections.constructor(CartProductResponse.class,
                        qProductVariants.id,
                        qCart.quantity,
                        qProduct.name,
                        qUploadFile.originUrl,
                        qProductVariants.price.add(qCart.quantity)
                ))
                .from(qCart)
                .join(qProductVariants).on(qCart.productVariantId.eq(qProductVariants.id))
                .join(qProduct).on(qProductVariants.productId.eq(qProduct.id))
                .leftJoin(qProductImage).on(qProduct.id.eq(qProductImage.productId))
                .leftJoin(qUploadFile).on(qProductImage.imageId.eq(qUploadFile.id))
                .where(qCart.userId.eq(userId),
                        qProductImage.type.eq(ProductImageType.MAIN).or(qProductImage.isNull()),
                        qProductVariants.deleted.eq(false),
                        qProduct.deleted.eq(false)
                )
                .limit(pageable.getPageSize())
                .fetchResults();

        results.getResults().forEach(cartProductResponse -> {
            Map<String, String> productOptionalValue = query()
                    .select(qProductOption.name, qProductOptionValue.value)
                    .from(qProductVariantOptionValues)
                    .join(qProductOptionValue).on(qProductVariantOptionValues.productOptionValueId.eq(qProductOptionValue.id))
                    .join(qProductOption).on(qProductOptionValue.product_option_id.eq(qProductOption.id))
                    .where(qProductVariantOptionValues.productVariantId.eq(cartProductResponse.getProductVariantId()))
                    .fetch()
                    .stream()
                    .filter(entry -> entry.get(qProductOption.name) != null && entry.get(qProductOptionValue.value) != null)
                    .collect(Collectors.toMap(
                            entry -> entry.get(qProductOption.name),
                            entry -> entry.get(qProductOptionValue.value),
                            (existing, replacement) -> replacement
                    ));
            cartProductResponse.setProductOptionalValue(productOptionalValue);

            Integer discountPercentage = query()
                    .select(qDiscount.discountPercentages)
                    .from(qDiscount)
                    .where(qDiscount.productId.eq(cartProductResponse.getProductVariantId())
                            .and(qDiscount.timeEnded.after(Timestamp.valueOf(LocalDateTime.now())))
                            .and(qDiscount.deleted.eq(false))
                            .and(qDiscount.timeStarted.before(Timestamp.valueOf(LocalDateTime.now())))
                    )
                    .orderBy(qDiscount.discountPercentages.desc())
                    .fetchFirst();

            cartProductResponse.setSalePrice(discountPercentage != null ? cartProductResponse.getOriginalPrice() * (100 - discountPercentage) / 100 * cartProductResponse.getQuantity() : null);
        });

        return new PageImpl<>(results.getResults(), pageable, results.getTotal());
    }


}

package com.dev.NT_Badminton.repositories.cart;


import com.dev.NT_Badminton.dto.response.cart.CartProductResponse;
import com.dev.NT_Badminton.entities.carts.QCart;
import com.dev.NT_Badminton.entities.discounts.QDiscount;
import com.dev.NT_Badminton.entities.products.*;
import com.dev.NT_Badminton.entities.products.constant.ProductImageType;
import com.dev.NT_Badminton.entities.upload_file.QUploadFile;
import com.dev.NT_Badminton.repositories.BaseRepository;
import com.dev.NT_Badminton.repositories.discount.DiscountRepository;
import com.dev.NT_Badminton.repositories.product.ProductOptionValueRepository;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.Projections;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class CartRepositoryImpl extends BaseRepository implements CartRepositoryCustom {

    ProductOptionValueRepository productOptionValueRepository;
    DiscountRepository discountRepository;

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
                        qProductVariants.price
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
            cartProductResponse.setProductOptionalValue(productOptionValueRepository.getProductVariantOptionValuesByProductVariantId(cartProductResponse.getProductVariantId()));
            Integer discountPercentage = discountRepository.getNewestUnexpiredDiscountWithHighestPercentage(cartProductResponse.getProductVariantId());

            cartProductResponse.setSalePrice(cartProductResponse.getOriginalPrice() * (100 - discountPercentage) / 100 * cartProductResponse.getQuantity());
            cartProductResponse.setOriginalPrice(cartProductResponse.getOriginalPrice() * cartProductResponse.getQuantity());
        });

        return new PageImpl<>(results.getResults(), pageable, results.getTotal());
    }


}

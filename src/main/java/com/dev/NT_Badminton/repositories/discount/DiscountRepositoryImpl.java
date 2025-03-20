package com.dev.NT_Badminton.repositories.discount;

import com.dev.NT_Badminton.dto.response.discount.DiscountResponse;
import com.dev.NT_Badminton.entities.discounts.Discount;
import com.dev.NT_Badminton.entities.discounts.QDiscount;
import com.dev.NT_Badminton.entities.products.QProduct;
import com.dev.NT_Badminton.entities.products.QProductImage;
import com.dev.NT_Badminton.entities.upload_file.QUploadFile;
import com.dev.NT_Badminton.repositories.BaseRepository;
import com.querydsl.core.types.Projections;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class DiscountRepositoryImpl extends BaseRepository implements DiscountRepositoryCustom {


    @Override
    public Optional<Discount> findHighestUnexpiredDiscountOfProduct(Integer productId) {
        QDiscount qDiscount = QDiscount.discount;
        return Optional.ofNullable(query()
                .selectFrom(qDiscount)
                .where(
                        qDiscount.productId.eq(productId)
                                .and(qDiscount.timeEnded.after(Timestamp.valueOf(LocalDateTime.now())))
                                .and(qDiscount.deleted.eq(false))
                                .and(qDiscount.timeStarted.before(Timestamp.valueOf(LocalDateTime.now())))
                )
                .orderBy(qDiscount.discountPercentages.desc())
                .fetchFirst());
    }

    @Override
    public List<DiscountResponse> findAllUnexpiredDiscounts() {
        QDiscount qDiscount = QDiscount.discount;
        QProduct qProduct = QProduct.product;
        QProductImage qProductImage = QProductImage.productImage;
        QUploadFile qUploadFile = QUploadFile.uploadFile;
        return query().
                select(Projections.constructor(
                        DiscountResponse.class,
                        qDiscount.id,
                        qDiscount.discountPercentages,
                        qDiscount.description,
                        qDiscount.timeStarted,
                        qDiscount.timeEnded,
                        qProduct.id,
                        qProduct.name,
                        qUploadFile.originUrl
                ))
                .from(qDiscount)
                .join(qProduct).on(qDiscount.productId.eq(qProduct.id))
                .leftJoin(qProductImage).on(qProduct.id.eq(qProductImage.productId))
                .leftJoin(qUploadFile).on(qProductImage.imageId.eq(qUploadFile.id))
                .where(
                        qDiscount.deleted.eq(false)
                        .and(qDiscount.timeEnded.after(Timestamp.valueOf(LocalDateTime.now())))
                        .and(qDiscount.timeStarted.before(Timestamp.valueOf(LocalDateTime.now())))
                )
                .fetch();
    }

    @Override
    public List<DiscountResponse> findAllDiscounts() {
        QDiscount qDiscount = QDiscount.discount;
        QProduct qProduct = QProduct.product;
        QProductImage qProductImage = QProductImage.productImage;
        QUploadFile qUploadFile = QUploadFile.uploadFile;
        return query().
                select(Projections.constructor(
                        DiscountResponse.class,
                        qDiscount.id,
                        qDiscount.discountPercentages,
                        qDiscount.description,
                        qDiscount.timeStarted,
                        qDiscount.timeEnded,
                        qProduct.id,
                        qProduct.name,
                        qUploadFile.originUrl
                ))
                .from(qDiscount)
                .join(qProduct).on(qDiscount.productId.eq(qProduct.id))
                .leftJoin(qProductImage).on(qProduct.id.eq(qProductImage.productId))
                .leftJoin(qUploadFile).on(qProductImage.imageId.eq(qUploadFile.id))
                .where(
                        qDiscount.deleted.eq(false)
                )
                .fetch();
    }

    @Override
    public Integer getNewestUnexpiredDiscountWithHighestPercentage(Integer productVariantId) {
        QDiscount qDiscount = QDiscount.discount;
        Integer discountPercentage = query()
                .select(qDiscount.discountPercentages)
                .from(qDiscount)
                .where(qDiscount.productId.eq(productVariantId)
                        .and(qDiscount.timeEnded.after(Timestamp.valueOf(LocalDateTime.now())))
                        .and(qDiscount.deleted.eq(false))
                        .and(qDiscount.timeStarted.before(Timestamp.valueOf(LocalDateTime.now())))
                )
                .orderBy(qDiscount.discountPercentages.desc())
                .fetchFirst();
        return discountPercentage == null ? 0 : discountPercentage;
    }
}

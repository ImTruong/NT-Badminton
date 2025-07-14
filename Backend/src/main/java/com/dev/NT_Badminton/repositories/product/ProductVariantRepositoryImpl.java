package com.dev.NT_Badminton.repositories.product;

import com.dev.NT_Badminton.entities.orders.QOrderItems;
import com.dev.NT_Badminton.entities.products.ProductVariants;
import com.dev.NT_Badminton.entities.products.QProductVariantOptionValues;
import com.dev.NT_Badminton.entities.products.QProductVariants;
import com.dev.NT_Badminton.repositories.BaseRepository;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAUpdateClause;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class ProductVariantRepositoryImpl extends BaseRepository implements ProductVariantRepositoryCustom {


    @Override
    public Optional<ProductVariants> findProductVariantByProductVariantOptionValuesIds(List<Integer> productVariantOptionValuesIds) {
        QProductVariants qProductVariants = QProductVariants.productVariants;
        QProductVariantOptionValues qProductVariantOptionValues = QProductVariantOptionValues.productVariantOptionValues;
        return Optional.ofNullable(query().
                selectFrom(qProductVariants).
                join(qProductVariantOptionValues).on(qProductVariants.id.eq(qProductVariantOptionValues.productVariantId))
                .where(
                        qProductVariantOptionValues.productOptionValueId.in(productVariantOptionValuesIds),
                        qProductVariants.deleted.eq(false)
                )
                .groupBy(qProductVariants.id)
                .having(qProductVariants.id.count().eq(Long.parseLong(String.valueOf(productVariantOptionValuesIds.size()))))
                .fetchFirst());
    }

    @Override
    public void increaseProductVariantQuantityFromCanceledOrder(int orderId) {
        QOrderItems qOrderItems = QOrderItems.orderItems;
        QProductVariants qProductVariants = QProductVariants.productVariants;
        new JPAUpdateClause(getEntityManager(), qProductVariants)
                .where(qProductVariants.id.in(
                        JPAExpressions.select(qOrderItems.productVariantId)
                                .from(qOrderItems)
                                .where(qOrderItems.orderId.eq(orderId))
                ))
                .set(qProductVariants.quantity, qProductVariants.quantity.add(
                        JPAExpressions.select(qOrderItems.quantity)
                                .from(qOrderItems)
                                .where(qOrderItems.productVariantId.eq(qProductVariants.id)
                                        .and(qOrderItems.orderId.eq(orderId)))
                ))
                .execute();
        getEntityManager().flush();
        getEntityManager().clear();
    }

    @Override
    public void decreaseProductVariantQuantityFromOrder(int orderId) {
        QOrderItems qOrderItems = QOrderItems.orderItems;
        QProductVariants qProductVariants = QProductVariants.productVariants;

        new JPAUpdateClause(getEntityManager(), qProductVariants)
                .where(qProductVariants.id.in(
                        JPAExpressions.select(qOrderItems.productVariantId)
                                .from(qOrderItems)
                                .where(qOrderItems.orderId.eq(orderId))
                ))
                .set(qProductVariants.quantity, qProductVariants.quantity.subtract(
                        JPAExpressions.select(qOrderItems.quantity)
                                .from(qOrderItems)
                                .where(qOrderItems.productVariantId.eq(qProductVariants.id)
                                        .and(qOrderItems.orderId.eq(orderId)))
                ))
                .execute();

        getEntityManager().flush();
        getEntityManager().clear();
    }

}

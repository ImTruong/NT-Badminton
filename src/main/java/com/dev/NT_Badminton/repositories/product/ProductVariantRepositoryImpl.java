package com.dev.NT_Badminton.repositories.product;

import com.dev.NT_Badminton.entities.products.ProductVariants;
import com.dev.NT_Badminton.entities.products.QProductVariantOptionValues;
import com.dev.NT_Badminton.entities.products.QProductVariants;
import com.dev.NT_Badminton.repositories.BaseRepository;

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

}

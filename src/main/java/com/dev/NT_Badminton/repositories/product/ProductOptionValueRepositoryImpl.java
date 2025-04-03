package com.dev.NT_Badminton.repositories.product;

import com.dev.NT_Badminton.entities.products.QProductOption;
import com.dev.NT_Badminton.entities.products.QProductOptionValue;
import com.dev.NT_Badminton.entities.products.QProductVariantOptionValues;
import com.dev.NT_Badminton.repositories.BaseRepository;

import java.util.Map;
import java.util.stream.Collectors;

public class ProductOptionValueRepositoryImpl extends BaseRepository  implements ProductOptionValueRepositoryCustom{
    @Override
    public Map<String, String> getProductVariantOptionValuesByProductVariantId(Integer productVariantId) {
        QProductOption qProductOption = QProductOption.productOption;
        QProductVariantOptionValues qProductVariantOptionValues = QProductVariantOptionValues.productVariantOptionValues;
        QProductOptionValue qProductOptionValue = QProductOptionValue.productOptionValue;
        Map<String, String> result = query()
                .select(qProductOption.name, qProductOptionValue.value)
                .from(qProductVariantOptionValues)
                .join(qProductOptionValue).on(qProductVariantOptionValues.productOptionValueId.eq(qProductOptionValue.id))
                .join(qProductOption).on(qProductOptionValue.productOptionId.eq(qProductOption.id))
                .where(qProductVariantOptionValues.productVariantId.eq(productVariantId))
                .fetch()
                .stream()
                .filter(entry -> entry.get(qProductOption.name) != null && entry.get(qProductOptionValue.value) != null)
                .collect(Collectors.toMap(
                        entry -> entry.get(qProductOption.name),
                        entry -> entry.get(qProductOptionValue.value),
                        (existing, replacement) -> replacement
                ));
        return result;
    }
}

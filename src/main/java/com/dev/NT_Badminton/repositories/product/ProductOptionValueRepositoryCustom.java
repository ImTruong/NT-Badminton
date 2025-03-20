package com.dev.NT_Badminton.repositories.product;

import java.util.Map;

public interface ProductOptionValueRepositoryCustom {

    Map<String,String> getProductVariantOptionValuesByProductVariantId(Integer productVariantId);

}

package com.dev.NT_Badminton.services.discount;

import com.dev.NT_Badminton.dto.request.discount.ModifyDiscountRequest;
import com.dev.NT_Badminton.entities.discounts.Discount;

import java.util.Optional;

public interface DiscountService {

    Discount changeOrAddDiscount(ModifyDiscountRequest modifyDiscountRequest);

    Optional<Discount> getHighestUnexpiredDiscountOfProduct(Integer productId);

    void deleteDiscount(Integer discountId);

}

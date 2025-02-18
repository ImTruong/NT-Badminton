package com.dev.NT_Badminton.services.discount;

import com.dev.NT_Badminton.dto.request.discount.CreateDiscountRequest;
import com.dev.NT_Badminton.entities.discounts.Discount;

import java.util.Optional;

public interface DiscountService {
    Discount createDiscount(CreateDiscountRequest createDiscountRequest);

    Optional<Discount> getHighestUnexpiredDiscountOfProduct(Integer productId);


}

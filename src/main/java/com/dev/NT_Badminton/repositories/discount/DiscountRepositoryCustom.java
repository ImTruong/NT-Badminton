package com.dev.NT_Badminton.repositories.discount;

import com.dev.NT_Badminton.dto.response.discount.DiscountResponse;
import com.dev.NT_Badminton.entities.discounts.Discount;

import java.util.List;
import java.util.Optional;

public interface DiscountRepositoryCustom {

    Optional<Discount> findHighestUnexpiredDiscountOfProduct(Integer productId);

    List<DiscountResponse> findAllUnexpiredDiscounts();

    List<DiscountResponse> findAllDiscounts();

}

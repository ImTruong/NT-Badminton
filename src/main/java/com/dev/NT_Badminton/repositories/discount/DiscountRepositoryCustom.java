package com.dev.NT_Badminton.repositories.discount;

import com.dev.NT_Badminton.entities.discounts.Discount;

import java.util.Optional;

public interface DiscountRepositoryCustom {

    Optional<Discount> findHighestUnexpiredDiscountOfProduct(Integer productId);

}

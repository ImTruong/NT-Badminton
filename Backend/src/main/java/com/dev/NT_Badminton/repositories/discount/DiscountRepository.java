package com.dev.NT_Badminton.repositories.discount;

import com.dev.NT_Badminton.entities.discounts.Discount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DiscountRepository extends JpaRepository<Discount, Integer>, DiscountRepositoryCustom {

}

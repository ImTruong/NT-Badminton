package com.dev.NT_Badminton.repositories.discount;

import com.dev.NT_Badminton.entities.discounts.Discount;
import com.dev.NT_Badminton.entities.discounts.QDiscount;
import com.dev.NT_Badminton.repositories.BaseRepository;

import java.sql.Timestamp;
import java.time.LocalDateTime;
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
}

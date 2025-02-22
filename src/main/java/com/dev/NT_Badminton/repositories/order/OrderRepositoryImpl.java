package com.dev.NT_Badminton.repositories.order;

import com.dev.NT_Badminton.entities.orders.QOrder;
import com.dev.NT_Badminton.entities.orders.QOrderItems;
import com.dev.NT_Badminton.entities.products.QProductVariants;
import com.dev.NT_Badminton.repositories.BaseRepository;

public class OrderRepositoryImpl extends BaseRepository implements OrderRepositoryCustom{

    @Override
    public Integer getOrderTotalPrice(Integer orderId) {
        QOrderItems orderItems = QOrderItems.orderItems;
        QProductVariants productVariant = QProductVariants.productVariants;
        return query().select(orderItems.quantity.multiply(productVariant.price))
                .from(orderItems)
                .join(productVariant).on(orderItems.productVariantId.eq(productVariant.id))
                .fetch()
                .stream()
                .reduce(0, Integer::sum);
    }

}

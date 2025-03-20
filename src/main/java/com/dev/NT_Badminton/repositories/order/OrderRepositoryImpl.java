package com.dev.NT_Badminton.repositories.order;

import com.dev.NT_Badminton.dto.response.order.OrderItemResponse;
import com.dev.NT_Badminton.dto.response.order.OrderResponse;
import com.dev.NT_Badminton.dto.response.user.UserContactResponse;
import com.dev.NT_Badminton.entities.contacts.QContact;
import com.dev.NT_Badminton.entities.orders.QOrder;
import com.dev.NT_Badminton.entities.orders.QOrderItems;
import com.dev.NT_Badminton.entities.orders.constant.DeliveryStatus;
import com.dev.NT_Badminton.entities.orders.constant.PaymentStatus;
import com.dev.NT_Badminton.entities.products.QProductVariants;
import com.dev.NT_Badminton.repositories.BaseRepository;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQuery;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class OrderRepositoryImpl extends BaseRepository implements OrderRepositoryCustom{

    private final OrderItemRepository orderItemRepository;

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

    @Override
    public List<OrderResponse> getUserOrder(Integer userId, String orderType) {
        QOrder qOrder = QOrder.order;
        QOrderItems qOrderItems = QOrderItems.orderItems;
        QProductVariants qProductVariants = QProductVariants.productVariants;
        QContact qContact = QContact.contact;

        JPAQuery<OrderResponse> UnfinishQuery = query().select(Projections.bean(
                        OrderResponse.class,
                        qOrder.id.as("orderId"),
                        Expressions.cases()
                                .when(qOrder.deliveryStatus.eq(DeliveryStatus.PENDING)).then("PENDING")
                                .when(qOrder.deliveryStatus.eq(DeliveryStatus.DELIVERING)).then("DELIVERING")
                                .when(qOrder.deliveryStatus.eq(DeliveryStatus.SHIPPED)).then("SHIPPED")
                                .when(qOrder.deliveryStatus.eq(DeliveryStatus.CANCELLED)).then("CANCELLED")
                                .otherwise("UNKNOWN")
                                .as("deliveryStatus"),
                        Expressions.cases()
                                .when(qOrder.paymentStatus.eq(PaymentStatus.PENDING)).then("PENDING")
                                .when(qOrder.paymentStatus.eq(PaymentStatus.PAID)).then("PAID")
                                .when(qOrder.paymentStatus.eq(PaymentStatus.UNPAID)).then("UNPAID")
                                .otherwise("UNKNOWN")
                                .as("paymentStatus"),
                        Projections.bean(
                                UserContactResponse.class,
                                qContact.id,
                                qContact.firstName,
                                qContact.lastName,
                                qContact.phone,
                                qContact.email,
                                qContact.city,
                                qContact.district,
                                qContact.streetAddress
                        ).as("userContact"),
                        Expressions.cases()
                                .when(qOrder.deleted.isTrue()).then(true)
                                .otherwise(false).as("isCanceled")
                )).from(qOrder)
                .join(qContact).on(qOrder.contactId.eq(qContact.id))
                .where(qOrder.userId.eq(userId));
        if (orderType!=null && orderType.equals("UNPAID")) {
            UnfinishQuery.where(qOrder.paymentStatus.eq(PaymentStatus.PENDING)
                    .or(qOrder.paymentStatus.eq(PaymentStatus.UNPAID))
                    .and(qOrder.deleted.isFalse()));
        } else if (orderType!=null && orderType.equals("DELIVERING")) {
            UnfinishQuery.where(qOrder.deliveryStatus.eq(DeliveryStatus.DELIVERING)
                    .and(qOrder.deleted.isFalse()));
        } else if (orderType!=null && orderType.equals("FINISHED")) {
            UnfinishQuery.where(qOrder.deliveryStatus.eq(DeliveryStatus.SHIPPED)
                    .and(qOrder.deleted.isFalse()));
        } else if (orderType!=null && orderType.equals("CANCELED")) {
            UnfinishQuery.where(qOrder.deleted.isTrue());
        }
        List<OrderResponse> result = UnfinishQuery.fetch();
        result.forEach(orderResponse -> {
            List<OrderItemResponse> orderItemList = orderItemRepository.getAllItemResponseByOrderId(orderResponse.getOrderId());
            orderResponse.setOrderItemList(orderItemRepository.getAllItemResponseByOrderId(orderResponse.getOrderId()));
            orderResponse.setTotalPrice(orderResponse.getOrderItemList().stream().mapToDouble(OrderItemResponse::getPrice).sum());
            orderResponse.setTotalPriceAfterDiscount(orderResponse.getOrderItemList().stream().mapToDouble(OrderItemResponse::getPriceAfterDiscount).sum());
        });
        return result;
    }

}

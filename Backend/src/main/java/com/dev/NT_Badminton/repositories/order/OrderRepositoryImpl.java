package com.dev.NT_Badminton.repositories.order;

import com.dev.NT_Badminton.dto.response.order.OrderContactResponse;
import com.dev.NT_Badminton.dto.response.order.OrderItemResponse;
import com.dev.NT_Badminton.dto.response.order.OrderResponse;
import com.dev.NT_Badminton.entities.contacts.QCity;
import com.dev.NT_Badminton.entities.contacts.QContact;
import com.dev.NT_Badminton.entities.contacts.QDistrict;
import com.dev.NT_Badminton.entities.contacts.QWard;
import com.dev.NT_Badminton.entities.orders.QOrder;
import com.dev.NT_Badminton.entities.orders.QOrderItems;
import com.dev.NT_Badminton.entities.orders.constant.DeliveryStatus;
import com.dev.NT_Badminton.entities.orders.constant.PaymentStatus;
import com.dev.NT_Badminton.entities.products.QProductVariants;
import com.dev.NT_Badminton.repositories.BaseRepository;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQuery;
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
                .where(orderItems.orderId.eq(orderId))
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
        QCity qCity = QCity.city;
        QDistrict qDistrict = QDistrict.district;
        QWard qWard = QWard.ward;

        JPAQuery<OrderResponse> UnfinishQuery = query().select(Projections.bean(
                        OrderResponse.class,
                        qOrder.id.as("orderId"),
                        Expressions.cases()
                                .when(qOrder.deliveryStatus.eq(DeliveryStatus.PENDING)).then("Chờ xác nhận")
                                .when(qOrder.deliveryStatus.eq(DeliveryStatus.DELIVERING)).then("Đang giao")
                                .when(qOrder.deliveryStatus.eq(DeliveryStatus.SHIPPED)).then("Đã giao")
                                .when(qOrder.deliveryStatus.eq(DeliveryStatus.CANCELLED)).then("Đã hủy")
                                .otherwise("UNKNOWN")
                                .as("deliveryStatus"),
                        Expressions.cases()
                                .when(qOrder.paymentStatus.eq(PaymentStatus.PENDING)).then("Chờ thanh toán")
                                .when(qOrder.paymentStatus.eq(PaymentStatus.PAID)).then("Đã thanh toán")
                                .when(qOrder.paymentStatus.eq(PaymentStatus.UNPAID)).then("Chưa thanh toán")
                                .otherwise("UNKNOWN")
                                .as("paymentStatus"),
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
            orderResponse.setContact(
                    query().select(Projections.bean(
                                    OrderContactResponse.class,
                                    qContact.firstName.as("firstName"),
                                    qContact.lastName.as("lastName"),
                                    qContact.phone.as("phone"),
                                    qContact.streetAddress.as("streetAddress"),
                                    qCity.name.as("city"),
                                    qDistrict.name.as("district"),
                                    qWard.name.as("ward")
                            ))
                            .from(qContact)
                            .leftJoin(qCity).on(qContact.cityId.eq(qCity.id))
                            .leftJoin(qDistrict).on(qContact.districtId.eq(qDistrict.id))
                            .leftJoin(qWard).on(qContact.wardId.eq(qWard.id))
                            .where(qContact.id.eq(
                                    query().select(qOrder.contactId)
                                            .from(qOrder)
                                            .where(qOrder.id.eq(orderResponse.getOrderId()))
                            )).fetchOne()
            );

            orderResponse.setOrderItemList(orderItemRepository.getAllItemResponseByOrderId(orderResponse.getOrderId()));
            orderResponse.setTotalPrice(orderResponse.getOrderItemList().stream().mapToDouble(OrderItemResponse::getPrice).sum());
        });
        return result;
    }

    @Override
    public Boolean existsByProductIdAndUserId(Integer productId, Integer userId) {
        QOrderItems qOrderItems = QOrderItems.orderItems;
        QProductVariants qProductVariants = QProductVariants.productVariants;
        QOrder qOrder = QOrder.order;
        return query().select(qOrderItems.id)
                .from(qOrderItems)
                .join(qProductVariants).on(qOrderItems.productVariantId.eq(qProductVariants.id))
                .join(qOrder).on(qOrderItems.orderId.eq(qOrder.id))
                .where(qProductVariants.productId.eq(productId)
                        .and(qOrder.userId.eq(userId))
                        .and(qOrder.deleted.isFalse())
                        .and(qOrder.deleted.isFalse()))
                .fetchFirst() != null;
    }

}

package com.dev.NT_Badminton.dto.response.order;

import com.dev.NT_Badminton.dto.response.user.UserContactResponse;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrderResponse {

    Integer orderId;

    List<OrderItemResponse> orderItemList;

    Double totalPrice;

    Double totalPriceAfterDiscount;

    String deliveryStatus;

    String paymentStatus;

    Boolean isCanceled;

    UserContactResponse contact;

    public OrderResponse(String deliveryStatus, String paymentStatus, UserContactResponse contact, Boolean isCanceled) {
        this.deliveryStatus = deliveryStatus;
        this.paymentStatus = paymentStatus;
        this.contact = contact;
        this.isCanceled = isCanceled;
    }

}

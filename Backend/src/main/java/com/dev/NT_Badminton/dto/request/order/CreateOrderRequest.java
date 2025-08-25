package com.dev.NT_Badminton.dto.request.order;

import com.dev.NT_Badminton.dto.service.VNPayRequest;
import com.dev.NT_Badminton.entities.orders.constant.PaymentMethod;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateOrderRequest {

    @NotNull(message = "orderItems is required")
    List<OrderItemRequest> orderItems;

    @NotNull(message = "contactId is required")
    Integer contactId;

    @NotNull(message = "paymentMethod is required")
    PaymentMethod paymentMethod;

    VNPayRequest vnpayRequest;

}

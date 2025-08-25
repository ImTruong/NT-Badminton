package com.dev.NT_Badminton.services.order;

import com.dev.NT_Badminton.dto.request.order.CreateOrderRequest;
import com.dev.NT_Badminton.dto.response.order.OrderResponse;
import com.dev.NT_Badminton.entities.orders.constant.DeliveryStatus;
import com.dev.NT_Badminton.entities.orders.constant.PaymentMethod;
import com.dev.NT_Badminton.entities.orders.constant.PaymentStatus;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

public interface OrderService {

    String checkOutFromCart(CreateOrderRequest orderRequest);

    void updateContact(Integer contactId, Integer orderId);

    void updatePaymentStatus(Integer orderId, PaymentStatus paymentStatus);

    boolean finishOnlinePayment(HttpServletRequest request);

    void cancelOrder(int orderId);

    void updateDeliveryStatus(Integer orderId, DeliveryStatus status);

    List<OrderResponse> getAllOrders();

    List<OrderResponse> getAllUnpaidOrders();

    List<OrderResponse> getAllDeliveringOrders();

    List<OrderResponse> getAllFinishedOrders();

    List<OrderResponse> getAllCanceledOrders();

    void finishCodOrder(int orderId);

    boolean checkOrderExistByProductIdAndUserId(Integer productId, Integer userId);


}

package com.dev.NT_Badminton.services.order;

import com.dev.NT_Badminton.dto.request.order.OrderItemRequest;
import com.dev.NT_Badminton.dto.response.order.OrderResponse;
import com.dev.NT_Badminton.entities.orders.Order;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;
import java.util.Map;

public interface OrderService {

    Integer checkOutFromCart(List<OrderItemRequest> orderItems);

    void updateContact(Integer contactId, Integer orderId);

    String updatePayment(HttpServletRequest request);

    boolean finishOnlinePayment(HttpServletRequest request);

    void cancelOrder(int orderId);

    void updateDeliveryStatus(int orderId, int status);

    List<OrderResponse> getAllOrders();

    List<OrderResponse> getAllUnpaidOrders();

    List<OrderResponse> getAllDeliveringOrders();

    List<OrderResponse> getAllFinishedOrders();

    List<OrderResponse> getAllCanceledOrders();

    void finishCodOrder(int orderId);

    boolean checkOrderExistByProductIdAndUserId(Integer productId, Integer userId);


}

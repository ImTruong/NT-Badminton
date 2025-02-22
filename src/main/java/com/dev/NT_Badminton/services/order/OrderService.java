package com.dev.NT_Badminton.services.order;

import com.dev.NT_Badminton.dto.request.order.OrderItemRequest;
import com.dev.NT_Badminton.entities.orders.Order;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

public interface OrderService {

    Order checkOutFromCart(List<OrderItemRequest> orderItems);

    void updateContact(Integer contactId, Integer orderId);

    String updatePayment(HttpServletRequest request);

}

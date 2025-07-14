package com.dev.NT_Badminton.repositories.order;

import com.dev.NT_Badminton.dto.response.order.OrderItemResponse;

import java.util.List;

public interface OrderItemRepositoryCustom {

    List<OrderItemResponse> getAllItemResponseByOrderId(Integer orderId);

}

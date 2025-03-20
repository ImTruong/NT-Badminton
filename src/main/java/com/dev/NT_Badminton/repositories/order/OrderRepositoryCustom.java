package com.dev.NT_Badminton.repositories.order;

import com.dev.NT_Badminton.dto.response.order.OrderResponse;

import java.util.List;

public interface OrderRepositoryCustom {

    Integer getOrderTotalPrice(Integer orderId);

    List<OrderResponse> getUserOrder(Integer userId , String orderType);

}

package com.dev.NT_Badminton.repositories.order;

import com.dev.NT_Badminton.entities.orders.OrderItems;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItems, Integer> {
}

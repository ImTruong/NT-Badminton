package com.dev.NT_Badminton.repositories.order;

import com.dev.NT_Badminton.entities.orders.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer>, OrderRepositoryCustom {

}

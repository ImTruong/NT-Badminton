package com.dev.NT_Badminton.repositories.cart;

import com.dev.NT_Badminton.entities.carts.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartRepository extends JpaRepository<Cart, Integer>, CartRepositoryCustom {
}

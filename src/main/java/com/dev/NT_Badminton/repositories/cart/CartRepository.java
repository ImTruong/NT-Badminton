package com.dev.NT_Badminton.repositories.cart;

import com.dev.NT_Badminton.entities.carts.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, Integer>, CartRepositoryCustom {

    List<Cart> findByUserId(Integer userId);

    Optional<Cart> findByUserIdAndProductVariantId(Integer userId, Integer productVariantId);

    void deleteAllByUserId(Integer userId);
}

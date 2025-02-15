package com.dev.NT_Badminton.services.cart;

import com.dev.NT_Badminton.dto.request.cart.AddToCartRequest;
import com.dev.NT_Badminton.entities.carts.Cart;
import com.dev.NT_Badminton.entities.products.Product;
import com.dev.NT_Badminton.entities.products.ProductOptionValue;
import com.dev.NT_Badminton.entities.users.AppUser;
import com.dev.NT_Badminton.exception.OutOfStockException;
import com.dev.NT_Badminton.repositories.cart.CartRepository;
import com.dev.NT_Badminton.services.product.ProductService;
import com.dev.NT_Badminton.services.user.UserService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CartServiceImpl implements CartService{

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private ProductService productService;

    @Transactional
    @Override
    public void addProductToCart(AddToCartRequest addToCartRequest) {
//        AppUser user = userService.getUserFromSecurityContext();
//        Integer quantity = null;
//
//            throw new OutOfStockException("This combination of product is out of stock");
//        cartRepository.save(new Cart(productId, user.getId(), quantity));
    }
}

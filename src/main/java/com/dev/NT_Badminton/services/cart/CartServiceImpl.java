package com.dev.NT_Badminton.services.cart;

import com.dev.NT_Badminton.entities.carts.Cart;
import com.dev.NT_Badminton.entities.products.Product;
import com.dev.NT_Badminton.entities.users.AppUser;
import com.dev.NT_Badminton.repositories.cart.CartRepository;
import com.dev.NT_Badminton.services.product.ProductService;
import com.dev.NT_Badminton.services.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CartServiceImpl implements CartService{

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private ProductService productService;

    @Override
    public void addProductToCart(int productId, int quantity) {
        AppUser user = userService.getUserFromSecurityContext();
        Product product = productService.getProductById(productId);
//        if product
        cartRepository.save(new Cart(productId, user.getId(), quantity));
    }
}

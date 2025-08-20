package com.dev.NT_Badminton.services.cart;

import com.dev.NT_Badminton.dto.request.cart.AddProductToCartRequest;
import com.dev.NT_Badminton.dto.request.cart.QuantityChangeRequest;
import com.dev.NT_Badminton.dto.response.cart.CartProductResponse;
import com.dev.NT_Badminton.entities.carts.Cart;
import com.dev.NT_Badminton.entities.products.ProductVariants;
import com.dev.NT_Badminton.entities.users.AppUser;
import com.dev.NT_Badminton.exception.OutOfStockException;
import com.dev.NT_Badminton.repositories.cart.CartRepository;
import com.dev.NT_Badminton.services.product.ProductService;
import com.dev.NT_Badminton.services.user.UserService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
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
    public Cart addProductToCart(AddProductToCartRequest addProductToCartRequest) {
        AppUser user = userService.getUserFromSecurityContext();
        ProductVariants cartProduct = productService.getProductVariantById(addProductToCartRequest.getProductVariantId());
        if (cartProduct.getQuantity() < addProductToCartRequest.getQuantity())
            throw new OutOfStockException("This combination of product is out of stock");
        Optional<Cart> cart = cartRepository.findByUserIdAndProductVariantId(user.getId(), cartProduct.getId());
        if(cart.isEmpty()){
            return cartRepository.save(new Cart(cartProduct.getId(), user.getId(), addProductToCartRequest.getQuantity()));
        } else {
            if (cart.get().getQuantity() + addProductToCartRequest.getQuantity() > cartProduct.getQuantity())
                throw new OutOfStockException("This combination of product is out of stock");
            cart.get().setQuantity(cart.get().getQuantity() + addProductToCartRequest.getQuantity());
            return cartRepository.save(cart.get());
        }

    }

    @Override
    public Cart changeProductQuantity(QuantityChangeRequest quantityChangeRequest) {
        ProductVariants productVariant = productService.getProductVariantById(quantityChangeRequest.getProductVariantId());
        if (productVariant.getQuantity() < quantityChangeRequest.getQuantity())
            throw new OutOfStockException("This combination of product is out of stock");
        if (quantityChangeRequest.getQuantity() <= 0)
            throw new IllegalArgumentException("Quantity must be greater than 0");
        AppUser user = userService.getUserFromSecurityContext();
        Optional<Cart> cart = cartRepository.findByUserIdAndProductVariantId(user.getId(), quantityChangeRequest.getProductVariantId());
        if(cart.isEmpty()){
            throw new IllegalArgumentException("Product not found in cart");
        } else {
            cart.get().setQuantity(quantityChangeRequest.getQuantity());
            return cartRepository.save(cart.get());
        }
    }

    @Override
    public void deleteProductFromCart(Integer productVariantId) {
        AppUser user = userService.getUserFromSecurityContext();
        Optional<Cart> cart = cartRepository.findByUserIdAndProductVariantId(user.getId(), productVariantId);
        if(cart.isEmpty()){
            throw new IllegalArgumentException("Product not found in cart");
        } else {
            cartRepository.delete(cart.get());
        }
    }

    @Transactional
    @Override
    public void deleteAllProductFromCart() {
        AppUser user = userService.getUserFromSecurityContext();
        cartRepository.deleteAllByUserId(user.getId());
    }

    @Override
    public PageImpl<CartProductResponse> getUserCart(Pageable pageable) {
        PageImpl<CartProductResponse> cartProductResponseByUserId = cartRepository.getCartProductResponseByUserId(userService.getUserFromSecurityContext().getId(), pageable);
        return cartProductResponseByUserId;
    }

    @Override
    public Cart checkProductExistenceInUserCart(Integer productVariantId, Integer userId) {
        return cartRepository.findByUserIdAndProductVariantId(userId, productVariantId).orElse(null);
    }


}

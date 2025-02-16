package com.dev.NT_Badminton.services.cart;

import com.dev.NT_Badminton.dto.request.cart.AddToCartRequest;
import com.dev.NT_Badminton.entities.carts.Cart;
import com.dev.NT_Badminton.entities.products.ProductVariants;
import com.dev.NT_Badminton.entities.users.AppUser;
import com.dev.NT_Badminton.exception.OutOfStockException;
import com.dev.NT_Badminton.repositories.cart.CartRepository;
import com.dev.NT_Badminton.services.product.ProductService;
import com.dev.NT_Badminton.services.user.UserService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
        AppUser user = userService.getUserFromSecurityContext();
        Integer productVariantId = null;
        if (addToCartRequest.getQuantity() <= 0)
            throw new IllegalArgumentException("Quantity must be greater than 0");
        if (addToCartRequest.getProductId()==null && (addToCartRequest.getProductOptionalValueId() == null || addToCartRequest.getProductOptionalValueId().isEmpty()))
            throw new IllegalArgumentException("Product id or product option value ids is required");
        if ((addToCartRequest.getProductOptionalValueId() == null || addToCartRequest.getProductOptionalValueId().isEmpty()) && productService.checkIfProductHasOption(addToCartRequest.getProductId()))
            throw new IllegalArgumentException("Product option value id is required for this product");
        if (addToCartRequest.getProductOptionalValueId() == null || addToCartRequest.getProductOptionalValueId().isEmpty()) {
            ProductVariants productVariant = productService.getProductVariantOfNonOptionedProduct(addToCartRequest.getProductId());
            if (productVariant.getQuantity() < addToCartRequest.getQuantity()) {
                throw new OutOfStockException("This combination of product is out of stock");
            }
            productVariantId = productVariant.getId();
        } else {
            ProductVariants productVariant = productService.getProductVariantByProductOptionValueIds(addToCartRequest.getProductOptionalValueId());
            if (productVariant.getQuantity() < addToCartRequest.getQuantity()) {
                throw new OutOfStockException("This combination of product is out of stock");
            }
            productVariantId = productVariant.getId();
        }

        cartRepository.save(new Cart(productVariantId, user.getId(), addToCartRequest.getQuantity()));
    }
}

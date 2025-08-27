package com.dev.NT_Badminton.services.order;

import com.dev.NT_Badminton.dto.request.order.CreateOrderRequest;
import com.dev.NT_Badminton.dto.response.order.OrderResponse;
import com.dev.NT_Badminton.entities.carts.Cart;
import com.dev.NT_Badminton.entities.contacts.Contact;
import com.dev.NT_Badminton.entities.orders.Order;
import com.dev.NT_Badminton.entities.orders.OrderItems;
import com.dev.NT_Badminton.entities.orders.constant.DeliveryStatus;
import com.dev.NT_Badminton.entities.orders.constant.PaymentMethod;
import com.dev.NT_Badminton.entities.orders.constant.PaymentStatus;
import com.dev.NT_Badminton.entities.products.ProductVariants;
import com.dev.NT_Badminton.entities.users.AppUser;
import com.dev.NT_Badminton.exception.*;
import com.dev.NT_Badminton.repositories.cart.CartRepository;
import com.dev.NT_Badminton.repositories.order.OrderItemRepository;
import com.dev.NT_Badminton.repositories.order.OrderRepository;
import com.dev.NT_Badminton.services.cart.CartService;
import com.dev.NT_Badminton.services.contact.ContactService;
import com.dev.NT_Badminton.services.order.payment.PaymentService;
import com.dev.NT_Badminton.services.product.ProductService;
import com.dev.NT_Badminton.services.user.UserService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final UserService userService;
    private final ModelMapper modelMapper;
    private final ContactService contactService;
    private final CartService cartService;
    private final ProductService productService;
    private final PaymentService paymentService;
    private final CartRepository cartRepository;

    @Transactional
    @Override
    public String checkOutFromCart(CreateOrderRequest orderRequest) {
        AppUser user = userService.getUserFromSecurityContext();
        Order order = Order.builder()
                .userId(user.getId())
                .contactId(orderRequest.getContactId())
                .paymentStatus(PaymentStatus.PENDING)
                .deliveryStatus(DeliveryStatus.PENDING)
                .paymentMethod(orderRequest.getPaymentMethod())
                .build();
        orderRepository.save(order);
        orderRequest.getOrderItems().forEach((item) -> {
            Cart cart = cartService.checkProductExistenceInUserCart(item.getProductVariantId(), user.getId());
            if (cart != null)
                cartService.deleteProductFromCart(cart.getProductVariantId());
            ProductVariants productVariant = productService.getProductVariantById(item.getProductVariantId());
            if (item.getQuantity() > productVariant.getQuantity())
                throw new OutOfStockException("Not enough quantity for product variant id: " + item.getProductVariantId());
            OrderItems orderItemEntity = OrderItems.builder()
                    .orderId(order.getId())
                    .productVariantId(item.getProductVariantId())
                    .quantity(item.getQuantity())
                    .build();
            orderItemRepository.save(orderItemEntity);
        });
        if (Objects.equals(orderRequest.getPaymentMethod(), PaymentMethod.VNPAY)){
            orderRequest.getVnpayRequest().setAmount(orderRepository.getOrderTotalPrice(order.getId()));
            orderRequest.getVnpayRequest().setOrderId(order.getId());
            return paymentService.createVnPayPayment(orderRequest);
        }else if(Objects.equals(orderRequest.getPaymentMethod(), PaymentMethod.COD)){
            updatePaymentStatus(order.getId(), PaymentStatus.UNPAID);
            updateDeliveryStatus(order.getId(), DeliveryStatus.DELIVERING);
            productService.changeQuantityOfProductDueToOrderAct(order.getId(), "order");
        }
        return null;
    }

    @Override
    public void updateContact(Integer contactId, Integer orderId) {
        AppUser user = userService.getUserFromSecurityContext();
        Contact contact = contactService.getContactById(contactId);
        if (contact.getUserId() != user.getId())
            throw new UnauthorizedException("You are not authorized user of this contact");
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new EntityNotFoundException("Order not found"));
        if (!order.getUserId().equals(user.getId()))
            throw new UnauthorizedException("You are not authorized to update this order");
        order.setContactId(contactId);
        orderRepository.save(order);
    }

    @Transactional
    @Override
    public void updatePaymentStatus(Integer orderId, PaymentStatus paymentStatus) {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new EntityNotFoundException("Order not found"));
        order.setPaymentStatus(paymentStatus);
        orderRepository.save(order);
    }

    @Transactional
    @Override
    public boolean finishOnlinePayment(HttpServletRequest request) {
        Integer orderId = Integer.parseInt(request.getParameter("vnp_OrderInfo"));
        String statusCode = request.getParameter("vnp_ResponseCode");
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new EntityNotFoundException("Order not found"));
        if(statusCode.equals("00")) {
            order.setPaymentStatus(PaymentStatus.PAID);
            updateDeliveryStatus(orderId, DeliveryStatus.DELIVERING);
            orderRepository.save(order);
            productService.changeQuantityOfProductDueToOrderAct(order.getId(), "order");
            return true;
        }
        return false;
    }

    @Transactional
    @Override
    public void cancelOrder(int orderId) {
        AppUser user = userService.getUserFromSecurityContext();
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new EntityNotFoundException("Order not found"));
        if (!order.getUserId().equals(user.getId()))
            throw new UnauthorizedException("You are not authorized to delete this order");
        if (order.getDeliveryStatus().equals(DeliveryStatus.SHIPPED))
            throw new OrderCycleException("You can not cancel shipped order");
        order.setDeleted(true);
        List<OrderItems> orderItems = orderItemRepository.findAllByOrderId(orderId);
        orderItems.forEach(orderItem -> {
            orderItem.setDeleted(true);
            orderItemRepository.save(orderItem);
        });
        productService.changeQuantityOfProductDueToOrderAct(orderId, "cancel");
        orderRepository.save(order);
    }

    @Transactional
    @Override
    public void updateDeliveryStatus(Integer orderId, DeliveryStatus status) {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new EntityNotFoundException("Order not found"));
        order.setDeliveryStatus(status);
        productService.changeQuantityOfProductDueToOrderAct(orderId, "order");
        orderRepository.save(order);
    }

    @Override
    public List<OrderResponse> getAllOrders() {
        AppUser user = userService.getUserFromSecurityContext();
        List<OrderResponse> orders = orderRepository.getUserOrder(user.getId(), null);
        return orders;
    }

    @Override
    public List<OrderResponse> getAllUnpaidOrders() {
        AppUser user = userService.getUserFromSecurityContext();
        List<OrderResponse> orders = orderRepository.getUserOrder(user.getId(), "UNPAID");
        return orders;
    }

    @Override
    public List<OrderResponse> getAllDeliveringOrders() {
        AppUser user = userService.getUserFromSecurityContext();
        List<OrderResponse> orders = orderRepository.getUserOrder(user.getId(), "DELIVERING");
        return orders;
    }

    @Override
    public List<OrderResponse> getAllFinishedOrders() {
        AppUser user = userService.getUserFromSecurityContext();
        List<OrderResponse> orders = orderRepository.getUserOrder(user.getId(), "FINISHED");
        return orders;
    }

    @Override
    public List<OrderResponse> getAllCanceledOrders() {
        AppUser user = userService.getUserFromSecurityContext();
        List<OrderResponse> orders = orderRepository.getUserOrder(user.getId(), "CANCELED");
        return orders;
    }

    @Transactional
    @Override
    public void finishCodOrder(int orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new EntityNotFoundException("Order not found"));
        if (!order.getPaymentMethod().equals(PaymentMethod.COD))
            throw new PaymentException("This order is not COD order");
        order.setPaymentStatus(PaymentStatus.PAID);
        order.setDeliveryStatus(DeliveryStatus.SHIPPED);
        orderRepository.save(order);
    }

    @Override
    public boolean checkOrderExistByProductIdAndUserId(Integer productId, Integer userId) {
        return orderRepository.existsByProductIdAndUserId(productId, userId);
    }


}

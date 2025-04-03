package com.dev.NT_Badminton.services.order;

import com.dev.NT_Badminton.dto.request.cart.AddProductToCartRequest;
import com.dev.NT_Badminton.dto.request.cart.QuantityChangeRequest;
import com.dev.NT_Badminton.dto.request.order.OrderItemRequest;
import com.dev.NT_Badminton.dto.response.order.OrderResponse;
import com.dev.NT_Badminton.entities.carts.Cart;
import com.dev.NT_Badminton.entities.contacts.Contact;
import com.dev.NT_Badminton.entities.orders.Order;
import com.dev.NT_Badminton.entities.orders.OrderItems;
import com.dev.NT_Badminton.entities.orders.constant.DeliveryStatus;
import com.dev.NT_Badminton.entities.orders.constant.PaymentMethod;
import com.dev.NT_Badminton.entities.orders.constant.PaymentStatus;
import com.dev.NT_Badminton.entities.users.AppUser;
import com.dev.NT_Badminton.exception.NotInPendingException;
import com.dev.NT_Badminton.exception.OrderCycleException;
import com.dev.NT_Badminton.exception.PaymentException;
import com.dev.NT_Badminton.exception.UnauthorizedException;
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
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

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
    public Integer checkOutFromCart(List<OrderItemRequest> orderItems) {
        AppUser user = userService.getUserFromSecurityContext();
        Order order = Order.builder()
                .userId(user.getId())
                .paymentStatus(PaymentStatus.PENDING)
                .deliveryStatus(DeliveryStatus.PENDING)
                .build();
        orderRepository.save(order);
        Map<Integer, Integer> groupedOrderItems = orderItems.stream()
                .collect(Collectors.toMap(
                        OrderItemRequest::getProductVariantId,
                        OrderItemRequest::getQuantity,
                        Integer::sum
                ));

        groupedOrderItems.forEach((productVariantId, quantity) -> {
            Cart cart = cartService.checkProductExistenceInUserCart(productVariantId, user.getId());
            if (cart == null) {
                cart = cartService.addProductToCart(
                        AddProductToCartRequest.builder()
                                .productVariantId(productVariantId)
                                .quantity(quantity)
                                .build()
                );
            } else {
                cart = cartService.changeProductQuantity(QuantityChangeRequest.builder()
                        .productVariantId(productVariantId)
                        .quantity(quantity)
                        .build());
            }

            OrderItems orderItemEntity = OrderItems.builder()
                    .orderId(order.getId())
                    .productVariantId(cart.getProductVariantId())
                    .quantity(cart.getQuantity())
                    .build();
            orderItemRepository.save(orderItemEntity);
        });
        return order.getId();
    }

    @Override
    public void updateContact(Integer contactId, Integer orderId) {
        AppUser user = userService.getUserFromSecurityContext();
        Contact contact = contactService.getContactById(contactId);
        if (contact.getUserId() != user.getId())
            throw new UnauthorizedException("You are not authorized user of this contact");
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new RuntimeException("Order not found"));
        if (!order.getUserId().equals(user.getId()))
            throw new UnauthorizedException("You are not authorized to update this order");
        if (!order.getDeliveryStatus().equals(DeliveryStatus.PENDING))
            throw new NotInPendingException("You can only update contact of pending orders");
        order.setContactId(contactId);
        orderRepository.save(order);
    }

    @Transactional
    @Override
    public String updatePayment(HttpServletRequest request) {
        AppUser user = userService.getUserFromSecurityContext();
        Integer orderId = Integer.parseInt(request.getParameter("orderId"));
        Integer payMethod = Integer.parseInt(request.getParameter("payMethod"));
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new EntityNotFoundException("Order not found"));
        if (!order.getUserId().equals(user.getId()))
            throw new UnauthorizedException("You are not authorized to update this order");
        if (order.getContactId() == null)
            throw new OrderCycleException("You must update contact before payment");
        if (!order.getPaymentStatus().equals(PaymentStatus.PENDING))
            throw new NotInPendingException("You can only update payment of pending orders");
        order.setPaymentMethod(PaymentMethod.fromValue(payMethod));
        order.setPaymentStatus(PaymentStatus.UNPAID);
        orderRepository.save(order);
        if (Objects.equals(payMethod, PaymentMethod.VNPAY.toValue())){
            request.setAttribute("orderId", orderId);
            request.setAttribute("amount", orderRepository.getOrderTotalPrice(orderId));
            return paymentService.createVnPayPayment(request);
        }else if(Objects.equals(payMethod, PaymentMethod.COD.toValue())){
            updateDeliveryStatus(orderId, DeliveryStatus.DELIVERING.toValue());
        }
        else
            throw new PaymentException("Invalid payment method");
        return null;
    }

    @Transactional
    @Override
    public boolean finishOnlinePayment(HttpServletRequest request) {
        Integer orderId = Integer.parseInt(request.getParameter("vnp_OrderInfo"));
        String statusCode = request.getParameter("vnp_ResponseCode");
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new EntityNotFoundException("Order not found"));
        if(statusCode.equals("00")) {
            order.setPaymentStatus(PaymentStatus.PAID);
            updateDeliveryStatus(orderId, DeliveryStatus.DELIVERING.toValue());
            orderRepository.save(order);
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
    public void updateDeliveryStatus(int orderId, int status) {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new EntityNotFoundException("Order not found"));
        order.setDeliveryStatus(DeliveryStatus.fromValue(status));
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

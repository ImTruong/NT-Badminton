package com.dev.NT_Badminton.services.order;

import com.dev.NT_Badminton.dto.request.cart.AddProductToCartRequest;
import com.dev.NT_Badminton.dto.request.cart.QuantityChangeRequest;
import com.dev.NT_Badminton.dto.request.order.OrderItemRequest;
import com.dev.NT_Badminton.entities.carts.Cart;
import com.dev.NT_Badminton.entities.contacts.Contact;
import com.dev.NT_Badminton.entities.orders.Order;
import com.dev.NT_Badminton.entities.orders.OrderItems;
import com.dev.NT_Badminton.entities.orders.constant.DeliveryStatus;
import com.dev.NT_Badminton.entities.orders.constant.PaymentMethod;
import com.dev.NT_Badminton.entities.orders.constant.PaymentStatus;
import com.dev.NT_Badminton.entities.users.AppUser;
import com.dev.NT_Badminton.exception.NotInPendingException;
import com.dev.NT_Badminton.exception.UnauthorizedException;
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

    @Transactional
    @Override
    public Order checkOutFromCart(List<OrderItemRequest> orderItems) {
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
                cartService.addProductToCart(
                        AddProductToCartRequest.builder()
                                .productVariantId(productVariantId)
                                .quantity(quantity)
                                .build()
                );
            }
            else{
                cartService.changeProductQuantity(QuantityChangeRequest.builder()
                        .productVariantId(productVariantId)
                        .quantity(quantity)
                        .build());
            }
            OrderItems orderItemEntity = modelMapper.map(cart, OrderItems.class);
            orderItemEntity.setOrderId(order.getId());
            orderItemRepository.save(orderItemEntity);
        });
        return order;
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
        Integer payMethod = Integer.parseInt(request.getParameter("orderId"));
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new EntityNotFoundException("Order not found"));
        if (!order.getUserId().equals(user.getId()))
            throw new UnauthorizedException("You are not authorized to update this order");
        if (!order.getPaymentStatus().equals(PaymentStatus.PENDING))
            throw new NotInPendingException("You can only update payment of pending orders");

        order.setPaymentMethod(PaymentMethod.fromValue(payMethod));
        order.setPaymentStatus(PaymentStatus.UNPAID);
        orderRepository.save(order);
        if (Objects.equals(payMethod, PaymentMethod.VNPAY.toValue())){
            request.setAttribute("orderId", orderId);
            request.setAttribute("amount", orderRepository.getOrderTotalPrice(orderId));
            return paymentService.createVnPayPayment(request);
        }else{
            order.setDeliveryStatus(DeliveryStatus.DELIVERING);
            orderRepository.save(order);
        }
        return null;
    }
}

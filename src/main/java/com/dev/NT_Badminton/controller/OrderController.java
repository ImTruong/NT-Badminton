package com.dev.NT_Badminton.controller;

import com.dev.NT_Badminton.dto.request.order.OrderItemRequest;
import com.dev.NT_Badminton.dto.response.ApiResponse;
import com.dev.NT_Badminton.entities.orders.Order;
import com.dev.NT_Badminton.services.order.OrderService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/order")
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/checkout")
    public ResponseEntity<?> checkout(@RequestBody @Valid List<OrderItemRequest> orderItems) {
        orderService.checkOutFromCart(orderItems);
        ApiResponse<Order> response = new ApiResponse<Order>(true, "Checkout Successful");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/update-contact")
    public ResponseEntity<?> updateContact(@RequestParam @NotNull(message = "contactId is required") Integer contactId,
                                           @RequestParam @NotNull(message = "orderId is required") Integer orderId) {
        orderService.updateContact(contactId, orderId);
        ApiResponse<String> response = new ApiResponse<String>(true, "Update Contact Successful");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/update-payment")
    public ResponseEntity<?> updatePayment(HttpServletRequest request) {
        ApiResponse<String> response = new ApiResponse<String>(true, "Update Payment Successful", orderService.updatePayment(request));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}

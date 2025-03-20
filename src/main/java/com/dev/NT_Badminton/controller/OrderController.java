package com.dev.NT_Badminton.controller;

import com.dev.NT_Badminton.dto.request.order.OrderItemRequest;
import com.dev.NT_Badminton.dto.response.ApiResponse;
import com.dev.NT_Badminton.dto.response.order.OrderResponse;
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
import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/order")
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/checkout")
    public ResponseEntity<?> checkout(@RequestBody @Valid List<OrderItemRequest> orderItems) {
        ApiResponse<?> response = new ApiResponse<>(true, "Checkout Successful","Order id: " + orderService.checkOutFromCart(orderItems));
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

    @GetMapping("/finish-online-payment")
    public ResponseEntity<?> finishOnlinePayment(HttpServletRequest request) {
        boolean success = orderService.finishOnlinePayment(request);
        if (success == false)
            return new ResponseEntity<>(new ApiResponse<String>(false, "Payment Failed"), HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(new ApiResponse<String>(true, "Payment Successful"), HttpStatus.OK);
    }

    @PutMapping("/delivery-status")
    public ResponseEntity<?> updateDeliveryStatus(@RequestParam @NotNull(message = "orderId is required") int orderId,
                                                  @RequestParam @NotNull(message = "status is required") int status) {
        orderService.updateDeliveryStatus(orderId, status);
        ApiResponse<String> response = new ApiResponse<String>(true, "Update Delivery Status Successful");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping
    public ResponseEntity<?> cancelOrder(@RequestParam @NotNull(message = "orderId is required") int orderId) {
        orderService.cancelOrder(orderId);
        ApiResponse<String> response = new ApiResponse<String>(true, "Cancel Order Successful");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllOrders() {
        ApiResponse<?> response = new ApiResponse<>(true, "Get All Orders Successful", orderService.getAllOrders());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/unpaid")
    public ResponseEntity<?> getAllUnpaidOrders() {
        ApiResponse<?> response = new ApiResponse<>(true, "Get All Unpaid Orders Successful", orderService.getAllUnpaidOrders());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/delivering")
    public ResponseEntity<?> getAllDeliveringOrders() {
        ApiResponse<?> response = new ApiResponse<>(true, "Get All Delivering Orders Successful", orderService.getAllDeliveringOrders());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/finished")
    public ResponseEntity<?> getAllFinishedOrders() {
        ApiResponse<?> response = new ApiResponse<>(true, "Get All Finished Orders Successful", orderService.getAllFinishedOrders());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/cancel")
    public ResponseEntity<?> getAllCancelOrders() {
        ApiResponse<?> response = new ApiResponse<>(true, "Get All Finished Orders Successful", orderService.getAllCanceledOrders());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/cod")
    public ResponseEntity<?> finishCodOrder(@RequestParam @NotNull(message = "orderId is required") int orderId) {
        orderService.finishCodOrder(orderId);
        ApiResponse<String> response = new ApiResponse<String>(true, "Finish COD Order Successful");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}

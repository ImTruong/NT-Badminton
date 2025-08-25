package com.dev.NT_Badminton.services.order.payment;

import com.dev.NT_Badminton.dto.request.order.CreateOrderRequest;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


public interface PaymentService {

    String createVnPayPayment(CreateOrderRequest request);

}

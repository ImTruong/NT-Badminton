package com.dev.NT_Badminton.services.order.payment;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


public interface PaymentService {

    String createVnPayPayment(HttpServletRequest request);

}

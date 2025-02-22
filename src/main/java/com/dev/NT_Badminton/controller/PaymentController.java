package com.dev.NT_Badminton.controller;

import com.dev.NT_Badminton.dto.response.ApiResponse;
import com.dev.NT_Badminton.dto.response.payment.VNPayResponse;
import com.dev.NT_Badminton.services.order.payment.PaymentService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/payment")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @GetMapping("/vnpay/callback")
    public ResponseEntity<?> payCallbackHandler(HttpServletRequest request) {
        String status = request.getParameter("vnp_ResponseCode");
        if (status.equals("00")) {
            return new ResponseEntity<>(new VNPayResponse("00", "Success"), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new VNPayResponse(status, "Fail"), HttpStatus.OK);
        }
    }

}

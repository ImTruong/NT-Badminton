package com.dev.NT_Badminton.services.order.payment;

import com.dev.NT_Badminton.config.VNPayConfig;
import com.dev.NT_Badminton.dto.request.order.CreateOrderRequest;
import com.dev.NT_Badminton.util.VNPayUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;

@RequiredArgsConstructor
@Service

public class PaymentServiceImpl implements PaymentService {

    private final VNPayConfig vnPayConfig;

    public String createVnPayPayment(CreateOrderRequest request) {
        long amount = request.getVnpayRequest().getAmount() * 100L;
        String bankCode = request.getVnpayRequest().getBankCode();
        Map<String, String> vnpParamsMap = vnPayConfig.getVNPayConfig(String.valueOf(request.getVnpayRequest().getOrderId()));
        vnpParamsMap.put("vnp_Amount", String.valueOf(amount));
        if (bankCode != null && !bankCode.isEmpty()) {
            vnpParamsMap.put("vnp_BankCode", "NCB");
        }
        vnpParamsMap.put("vnp_IpAddr", request.getVnpayRequest().getIpAddress());
        String queryUrl = VNPayUtil.getPaymentURL(vnpParamsMap, true);
        String hashData = VNPayUtil.getPaymentURL(vnpParamsMap, false);
        String vnpSecureHash = VNPayUtil.hmacSHA512(vnPayConfig.getSecretKey(), hashData);
        queryUrl += "&vnp_SecureHash=" + vnpSecureHash;
        String paymentUrl = vnPayConfig.getVnp_PayUrl() + "?" + queryUrl;
        return paymentUrl;
    }

}

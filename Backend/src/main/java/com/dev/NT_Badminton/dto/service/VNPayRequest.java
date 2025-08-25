package com.dev.NT_Badminton.dto.service;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class VNPayRequest {

    Integer orderId;

    Integer amount;

    String bankCode;

    String ipAddress;

}

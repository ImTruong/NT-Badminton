package com.dev.NT_Badminton.dto.request.discount;


import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateDiscountRequest {

    @NotNull(message = "Discount percentage is required")
    int discountPercentages;

    String description;

    @NotNull(message = "Time started is required")
    Date timeStarted;

    @NotNull(message = "Time ended is required")
    Date timeEnded;

    @NotNull(message = "Status is required")
    Integer productId;

}

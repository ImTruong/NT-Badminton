package com.dev.NT_Badminton.dto.request.order;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderItemRequest {

    @NotNull(message = "Product variant id is required")
    Integer productVariantId;

    @NotNull(message = "Quantity is required")
    Integer quantity;

}

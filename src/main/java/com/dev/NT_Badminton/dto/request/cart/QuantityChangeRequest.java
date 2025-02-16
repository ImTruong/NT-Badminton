package com.dev.NT_Badminton.dto.request.cart;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuantityChangeRequest {

    @NotNull(message = "Product variant id is required")
    private Integer productVariantId;

    @NotNull(message = "Quantity is required")
    private Integer quantity;

}

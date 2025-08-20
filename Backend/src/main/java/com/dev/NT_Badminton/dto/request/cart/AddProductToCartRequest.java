package com.dev.NT_Badminton.dto.request.cart;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AddProductToCartRequest {

    private Integer productVariantId;

    @NotNull(message = "Quantity is required")
    private Integer quantity;


}

package com.dev.NT_Badminton.dto.request.cart;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AddToCartRequest {

    Integer productId;

    List<Integer> productOptionalValueId;

    Integer quantity;

}

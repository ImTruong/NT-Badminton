package com.dev.NT_Badminton.dto.response.discount;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DiscountResponse {

    Integer discountId;

    Integer discountPercentages;

    String description;

    Date timeStarted;

    Date timeEnded;

    Integer productId;

    String productName;

    String productImage;

}

package com.dev.NT_Badminton.dto.response.order;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrderContactResponse {

    String firstName;

    String lastName;

    String phone;

    String email;

    String streetAddress;

    String note;

    String city;

    String district;

    String ward;

}

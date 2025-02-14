package com.dev.NT_Badminton.dto.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class UserContactRequest {

    String firstName;

    String lastName;

    String phone;

    String email;

    Integer city;

    Integer district;

    String streetAddress;

    String note;

    String type;

}

package com.dev.NT_Badminton.dto.response.user;

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

public class UserContactResponse {

    Integer id;

    String firstName;

    String lastName;

    String phone;

    String email;

    Integer city;

    Integer district;

    String streetAddress;

    String type;

}

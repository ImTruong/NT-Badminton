package com.dev.NT_Badminton.dto.response.contact;

import com.dev.NT_Badminton.entities.contacts.ContactType;
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
public class UserContactResponse {

    Integer id;

    String firstName;

    String lastName;

    String phone;

    String email;

    String streetAddress;

    String note;

    ContactType type;

    Integer userId;

    CityResponse city;

    DistrictResponse district;

    WardResponse ward;

}

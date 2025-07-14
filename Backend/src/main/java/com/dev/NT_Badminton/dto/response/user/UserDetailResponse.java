package com.dev.NT_Badminton.dto.response.user;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.util.Date;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDetailResponse {

    String firstName;

    String lastName;

    String email;

    String phone;

    Integer city;

    Integer district;

    String streetAddress;

    String avatarUrl;

    Integer gender;

    Date birthday;

}

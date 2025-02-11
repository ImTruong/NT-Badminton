package com.dev.NT_Badminton.dto.response;


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

    String address;

    String avatarUrl;

    Integer gender;

    Date birthday;

}

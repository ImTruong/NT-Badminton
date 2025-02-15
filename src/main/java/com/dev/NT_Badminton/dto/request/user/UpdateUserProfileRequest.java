package com.dev.NT_Badminton.dto.request.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateUserProfileRequest {

    @Pattern(regexp = "^[a-zA-Z ]*$", message = "FirstName must not contain special characters")
    private String firstName;

    @Pattern(regexp = "^[a-zA-Z ]*$", message = "LastName must not contain special characters")
    private String lastName;

    @Email(message = "Invalid email format")
    private String email;

    @Pattern(regexp = "^[0-9]*$", message = "Phone number must not contain special characters")
    private String phone;

    private Date birthday;

    private int gender;

    Integer city;

    Integer district;

    String streetAddress;

    private MultipartFile avatar;
}

package com.dev.NT_Badminton.dto.request.user;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;

@Getter
@Setter
public class RegisterRequest {

    @NotBlank(message = "FirstName is required")
    @Pattern(regexp = "^[a-zA-Z ]*$", message = "FirstName must not contain special characters")
    private String firstName;

    @NotBlank(message = "LastName is required")
    @Pattern(regexp = "^[a-zA-Z ]*$", message = "LastName must not contain special characters")
    private String lastName;

    @Email(message = "Invalid email format")
    @NotBlank(message = "Email is required")
    private String email;

    @NotBlank(message = "Password is required")
    private String password;

    @Pattern(regexp = "^[0-9]*$", message = "Phone number must not contain special characters")
    private String phone;

    private Date birthday;

    @NotNull(message = "Gender is required")
    private int gender;

    Integer city;

    Integer district;

    String streetAddress;

    private MultipartFile avatar;

}

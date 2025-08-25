package com.dev.NT_Badminton.dto.request.contact;

import com.dev.NT_Badminton.entities.contacts.ContactType;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
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

    @NotBlank(message = "FirstName is required")
    @Pattern(regexp = "^[a-zA-Z ]*$", message = "FirstName must not contain special characters")
    private String firstName;

    @NotBlank(message = "LastName is required")
    @Pattern(regexp = "^[a-zA-Z ]*$", message = "LastName must not contain special characters")
    private String lastName;

    @NotBlank(message = "Phone number is required")
    String phone;

    String email;

    @NotNull(message = "City is required")
    Integer cityId;

    @NotNull(message = "District is required")
    Integer districtId;

    @NotNull(message = "Ward is required")
    Integer wardId;

    @NotBlank(message = "Street address is required")
    String streetAddress;

    String note;

    @NotNull(message = "Type is required")
    ContactType type;

    Integer contactId;

}

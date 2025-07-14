package com.dev.NT_Badminton.controller;

import com.dev.NT_Badminton.dto.request.contact.UserContactRequest;
import com.dev.NT_Badminton.dto.request.user.*;
import com.dev.NT_Badminton.dto.response.ApiResponse;
import com.dev.NT_Badminton.services.user.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserService userService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest loginRequest) {
        ApiResponse<String> response = new ApiResponse<String>(true,"Login Successful",userService.login(loginRequest));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping(value = "/register", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_OCTET_STREAM_VALUE })
    public ResponseEntity<?> register(@Valid @RequestPart("registerRequest") RegisterRequest registerRequest,
                                      @RequestPart(value = "avatar", required = false) MultipartFile avatar) throws Exception {
        if (avatar != null && !avatar.isEmpty()) registerRequest.setAvatar(avatar);
        userService.register(registerRequest);
        ApiResponse<String> response = new ApiResponse<String>(true, "Registration Successful");
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/password")
    public ResponseEntity<?> updatePassword(@Valid @RequestBody UpdateUserPasswordRequest updateUserPasswordRequest) {
        ApiResponse<String> response = new ApiResponse<String>(userService.updatePassword(updateUserPasswordRequest),"Password Updated Successfully");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping(value = "/profile", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_OCTET_STREAM_VALUE })
    public ResponseEntity<?> updateProfile(@Valid @RequestPart("updateUserProfileRequest") UpdateUserProfileRequest updateUserProfileRequest,
                                           @RequestPart(value = "avatar", required = false) MultipartFile avatar) throws Exception {
        if (avatar != null && !avatar.isEmpty()) updateUserProfileRequest.setAvatar(avatar);
        ApiResponse<String> response = new ApiResponse<String>(userService.updateProfile(updateUserProfileRequest),"User Profile Updated Successfully");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/profile")
    public ResponseEntity<?> getUserDetail() {
        ApiResponse<?> response = new ApiResponse<>(true,"User Detail Fetched Successfully",userService.getUserDetail());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/genders")
    public ResponseEntity<?> getAllGenders() {
        ApiResponse<?> response = new ApiResponse<>(true, "Genders Fetched Successfully", userService.getAllGenders());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/cities-districts")
    public ResponseEntity<?> getAllCitiesAndDistricts() {
        ApiResponse<?> response = new ApiResponse<>(true, "Cities and Districts Fetched Successfully", userService.getAllCitiesAndDistricts());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/contacts")
    public ResponseEntity<?> getUserContacts() {
        ApiResponse<?> response = new ApiResponse<>(true, "User Contacts Fetched Successfully", userService.getUserContacts());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/contact")
    public ResponseEntity<?> deleteContact(@RequestParam int contactId) {
        userService.deleteContact(contactId);
        ApiResponse<String> response = new ApiResponse<String>(true, "Contact Deleted Successfully");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/contact")
    public ResponseEntity<?> addContact(@Valid @RequestBody UserContactRequest userContactRequest) {
        userService.addContact(userContactRequest);
        ApiResponse<?> response = new ApiResponse<>(true, "Contact Added Successfully");
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/switch-main-contact")
    public ResponseEntity<?> switchMainContact(@RequestParam @NotNull(message = "Contact Id is required") int contactId) {
        userService.switchMainContact(contactId);
        ApiResponse<String> response = new ApiResponse<String>(true, "Main Contact Switched Successfully");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/contact")
    public ResponseEntity<?> updateContact(@Valid @RequestBody UserContactRequest modifyContactRequest) {
        userService.updateContact(modifyContactRequest);
        ApiResponse<String> response = new ApiResponse<String>(true, "Contact Updated Successfully");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


}

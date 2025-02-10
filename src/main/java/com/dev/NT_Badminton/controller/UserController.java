package com.dev.NT_Badminton.controller;

import com.dev.NT_Badminton.dto.request.LoginRequest;
import com.dev.NT_Badminton.dto.request.RegisterRequest;
import com.dev.NT_Badminton.dto.request.UpdateUserPasswordRequest;
import com.dev.NT_Badminton.dto.request.UpdateUserProfileRequest;
import com.dev.NT_Badminton.dto.response.ApiResponse;
import com.dev.NT_Badminton.services.user.UserService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@FieldDefaults(level = AccessLevel.PUBLIC)
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

    @PutMapping("/profile")
    public ResponseEntity<?> updateProfile(@Valid @RequestBody UpdateUserProfileRequest updateUserProfileRequest) {
        ApiResponse<String> response = new ApiResponse<String>(userService.updateProfile(updateUserProfileRequest),"User Profile Updated Successfully");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}

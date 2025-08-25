package com.dev.NT_Badminton.controller;

import com.dev.NT_Badminton.dto.response.ApiResponse;
import com.dev.NT_Badminton.services.contact.ContactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/contact")
public class ContactController {

    @Autowired
    ContactService contactService;

    @GetMapping("/locations")
    public ResponseEntity<?> getLocations() {
        ApiResponse<?> response = new ApiResponse<>(true, "Get Locations Successful", contactService.getAllLocations());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}

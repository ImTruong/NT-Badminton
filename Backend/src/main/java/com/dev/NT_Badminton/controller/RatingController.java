package com.dev.NT_Badminton.controller;

import com.dev.NT_Badminton.dto.request.rating.CreateRatingRequest;
import com.dev.NT_Badminton.dto.response.ApiResponse;
import com.dev.NT_Badminton.services.rating.RatingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/rating")
@RequiredArgsConstructor
public class RatingController {

    private final RatingService ratingService;

    @PostMapping
    public ResponseEntity<?> createRating(@RequestBody @Valid CreateRatingRequest createRatingRequest) {
        ratingService.addRating(createRatingRequest);
        ApiResponse<String> response = new ApiResponse<String>(true, "Rating Created Successfully");
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @DeleteMapping("/{ratingId}")
    public ResponseEntity<?> deleteRating(@PathVariable Integer ratingId) {
        ratingService.deleteRating(ratingId);
        ApiResponse<String> response = new ApiResponse<String>(true, "Rating Deleted Successfully");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}

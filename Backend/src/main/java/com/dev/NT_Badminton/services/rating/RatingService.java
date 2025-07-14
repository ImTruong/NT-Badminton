package com.dev.NT_Badminton.services.rating;

import com.dev.NT_Badminton.dto.request.rating.CreateRatingRequest;

public interface RatingService {

    void addRating(CreateRatingRequest createRatingRequest);

    void deleteRating(Integer ratingId);



}

package com.dev.NT_Badminton.repositories.rating;

import com.dev.NT_Badminton.entities.rating.Rating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RatingRepository extends JpaRepository<Rating, Integer> {


}

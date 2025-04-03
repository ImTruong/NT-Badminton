package com.dev.NT_Badminton.repositories.rating;

import com.dev.NT_Badminton.entities.rating.Rating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RatingRepository extends JpaRepository<Rating, Integer> {

    Optional<Rating> findByUserIdAndProductId(Integer userId, Integer productId);

}

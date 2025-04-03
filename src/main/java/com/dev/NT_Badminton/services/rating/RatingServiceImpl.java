package com.dev.NT_Badminton.services.rating;

import com.dev.NT_Badminton.dto.request.rating.CreateRatingRequest;
import com.dev.NT_Badminton.entities.rating.Rating;
import com.dev.NT_Badminton.entities.users.AppUser;
import com.dev.NT_Badminton.repositories.rating.RatingRepository;
import com.dev.NT_Badminton.services.order.OrderService;
import com.dev.NT_Badminton.services.user.UserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class RatingServiceImpl implements RatingService {

    private final RatingRepository ratingRepository;
    private final ModelMapper modelMapper;
    private final UserService userService;
    private final OrderService orderService;

    @Override
    public void addRating(CreateRatingRequest createRatingRequest) {
        AppUser user = userService.getUserFromSecurityContext();
        ratingRepository.findByUserIdAndProductId(user.getId(), createRatingRequest.getProductId())
                .ifPresent(rating -> { throw new IllegalArgumentException("You have already rated this product"); });
        if (!orderService.checkOrderExistByProductIdAndUserId(createRatingRequest.getProductId(), user.getId()))
            throw new IllegalArgumentException("You can't rate this product");
        Rating rating = modelMapper.map(createRatingRequest, Rating.class);
        rating.setUserId(user.getId());
        ratingRepository.save(rating);
    }

    @Override
    public void deleteRating(Integer ratingId) {
        AppUser user = userService.getUserFromSecurityContext();
        Rating rating = ratingRepository.findById(ratingId).orElseThrow(() -> new EntityNotFoundException("Rating not found"));
        if (!rating.getUserId().equals(user.getId())) {
            throw new IllegalArgumentException("You can't delete this rating");
        }
        ratingRepository.deleteById(ratingId);
    }


}

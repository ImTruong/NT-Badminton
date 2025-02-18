package com.dev.NT_Badminton.services.discount;

import com.dev.NT_Badminton.dto.request.discount.CreateDiscountRequest;
import com.dev.NT_Badminton.entities.discounts.Discount;
import com.dev.NT_Badminton.entities.users.AppUser;
import com.dev.NT_Badminton.repositories.discount.DiscountRepository;
import com.dev.NT_Badminton.services.user.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DiscountServiceImpl implements DiscountService {

    private final DiscountRepository discountRepository;
    private final ModelMapper modelMapper;
    private final UserService userService;

    @Override
    public Discount createDiscount(CreateDiscountRequest createDiscountRequest) {
        Discount discount = modelMapper.map(createDiscountRequest, Discount.class);
        return discountRepository.save(discount);
    }

    @Override
    public Optional<Discount> getHighestUnexpiredDiscountOfProduct(Integer productId) {
        return discountRepository.findHighestUnexpiredDiscountOfProduct(productId);
    }

}

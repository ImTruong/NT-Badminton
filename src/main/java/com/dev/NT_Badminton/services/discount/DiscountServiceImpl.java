package com.dev.NT_Badminton.services.discount;

import com.dev.NT_Badminton.dto.request.discount.ModifyDiscountRequest;
import com.dev.NT_Badminton.entities.discounts.Discount;
import com.dev.NT_Badminton.entities.users.AppUser;
import com.dev.NT_Badminton.repositories.discount.DiscountRepository;
import com.dev.NT_Badminton.services.user.UserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DiscountServiceImpl implements DiscountService {

    private final DiscountRepository discountRepository;
    private final ModelMapper modelMapper;
    private final UserService userService;

    @Override
    public Discount changeOrAddDiscount(ModifyDiscountRequest modifyDiscountRequest) {
        Discount discount = null;
        if (modifyDiscountRequest.getType().equals("CREATE"))
            discount = modelMapper.map(modifyDiscountRequest, Discount.class);
        else{
            discount = discountRepository.findById(modifyDiscountRequest.getDiscountId()).orElseThrow(() -> new EntityNotFoundException("Discount not found"));
            modelMapper.map(modifyDiscountRequest, discount);
        }
        return discountRepository.save(discount);
    }

    @Override
    public Optional<Discount> getHighestUnexpiredDiscountOfProduct(Integer productId) {
        return discountRepository.findHighestUnexpiredDiscountOfProduct(productId);
    }

    @Override
    public void deleteDiscount(Integer discountId) {
        AppUser user = userService.getUserFromSecurityContext();
        Discount discount = discountRepository.findById(discountId).orElseThrow(() -> new IllegalArgumentException("Discount not found"));
        discount.setDeleted(true);
        discountRepository.save(discount);
    }



}

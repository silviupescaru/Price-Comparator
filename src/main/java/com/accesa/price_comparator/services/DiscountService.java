package com.accesa.price_comparator.services;

import com.accesa.price_comparator.entities.Discount;
import com.accesa.price_comparator.repositories.DiscountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DiscountService {
    private final DiscountRepository discountRepository;

    public List<Discount> getCurrentDiscounts() {
        LocalDate today = LocalDate.now();
        return discountRepository.findByFromDateLessThanEqualAndToDateGreaterThanEqual(today, today);
    }
}

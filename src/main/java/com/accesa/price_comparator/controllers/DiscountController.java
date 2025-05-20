package com.accesa.price_comparator.controllers;

import com.accesa.price_comparator.entities.Discount;
import com.accesa.price_comparator.services.DiscountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/discounts")
@RequiredArgsConstructor
public class DiscountController {
    private final DiscountService discountService;

    @GetMapping("/current")
    public ResponseEntity<List<Discount>> getCurrentDiscounts() {
        return ResponseEntity.ok(discountService.getCurrentDiscounts());
    }
}

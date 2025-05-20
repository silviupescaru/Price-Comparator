package com.accesa.price_comparator.services;

import com.accesa.price_comparator.entities.ProductPrice;
import com.accesa.price_comparator.repositories.ProductPriceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductPriceRepository priceRepository;

    public Map<String, Double> getBestPrices(List<String> productNames) {
        Map<String, Double> bestPrices = new HashMap<>();
        for (String productName : productNames) {
            List<ProductPrice> prices = priceRepository.findByProductProductName(productName);
            prices.stream()
                    .min(Comparator.comparing(ProductPrice::getPrice))
                    .ifPresent(p -> bestPrices.put(p.getStore(), p.getPrice()));
        }
        return bestPrices;
    }
}

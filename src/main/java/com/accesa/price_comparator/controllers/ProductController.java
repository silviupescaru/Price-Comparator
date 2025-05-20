package com.accesa.price_comparator.controllers;

import com.accesa.price_comparator.services.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @PostMapping("/best-prices")
    public ResponseEntity<Map<String, Double>> getBestPrices(@RequestBody List<String> productNames) {
        return ResponseEntity.ok(productService.getBestPrices(productNames));
    }
}

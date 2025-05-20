package com.accesa.price_comparator.repositories;

import com.accesa.price_comparator.entities.Product;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface ProductRepository extends CrudRepository<Product, String> {
    Optional<Product> findByProductName(String productName);
}

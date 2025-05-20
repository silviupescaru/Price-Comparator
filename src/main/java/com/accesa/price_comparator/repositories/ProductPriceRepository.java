package com.accesa.price_comparator.repositories;

import com.accesa.price_comparator.entities.ProductPrice;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDate;
import java.util.List;

public interface ProductPriceRepository extends CrudRepository<ProductPrice, Long> {
    List<ProductPrice> findByProductProductNameAndStore(String productName, String store);
    List<ProductPrice> findByProductProductName(String productName);
    List<ProductPrice> findByStoreAndDate(String store, LocalDate date);
}

package com.accesa.price_comparator.repositories;

import com.accesa.price_comparator.entities.Discount;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDate;
import java.util.List;

public interface DiscountRepository extends CrudRepository<Discount, Long> {
    List<Discount> findByFromDateLessThanEqualAndToDateGreaterThanEqual(LocalDate from, LocalDate to);
}

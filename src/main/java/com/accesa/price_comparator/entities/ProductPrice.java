package com.accesa.price_comparator.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Data
public class ProductPrice {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    private Product product;
    private String store;
    private LocalDate date;
    private Double price;
    private Double packageQuantity;
    private String packageUnit;
}

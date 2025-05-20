package com.accesa.price_comparator.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

@Entity
@Data
public class Product {

    @Id
    private String productId;
    private String productName;
    private String category;
    private String brand;
}

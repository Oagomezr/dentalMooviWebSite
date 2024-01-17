package com.dentalmoovi.website.models.entities;

import org.springframework.data.relational.core.mapping.Table;

import lombok.AllArgsConstructor;
import lombok.Data;

@Table("orders_products")
@Data
@AllArgsConstructor
public class ProductsRef {
    private Long idProduct;
    private int amount;
}

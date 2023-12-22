package com.dentalmoovi.website.models.entities;

import org.springframework.data.annotation.Id;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Products {
    @Id
    private Long id;
    @EqualsAndHashCode.Include
    private String name;
    private String description;
    private String shortDescription;
    private double unitPrice;
    private int stock;
    private boolean openToPublic;
    private Long idMainImage;
    private Long idCategory;
}

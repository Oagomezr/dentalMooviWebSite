package com.dentalmoovi.website.models.dtos;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductsDTO {
    private long id;
    private String nameProduct;
    private double unitPrice;
    private String description;
    private String shortDescription;
    private int stock;
    private List<ImagesDTO> images;
    private List<String> location;
    private String hidden;
}

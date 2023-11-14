package com.dentalmoovi.website.models.responses;

import java.util.List;

import com.dentalmoovi.website.models.dtos.ProductsDTO;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ProductsResponse {
    private int totalProducts;
    private int paginatedProducts;
    private List<ProductsDTO> data;
}

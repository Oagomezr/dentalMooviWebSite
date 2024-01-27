package com.dentalmoovi.website.models.cart;

import com.dentalmoovi.website.models.dtos.ImagesDTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CartDtoRespose {
    private long id;
    private ImagesDTO image;
    private String productName;
    private double prize;
    private int amount;
    private double subtotal;
    private String prizePDF;
    private String subtotalPDF;
}

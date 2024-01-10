package com.dentalmoovi.website.models.cart;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CartResponse {
    private List<CartDtoRespose> data;
    private double total;
    private int amountOfProducts;
}
